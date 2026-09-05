from flask import Flask, render_template, request, redirect, url_for, session
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)

app = Flask(__name__)
app.secret_key = "secret123"

# Database config
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///meditech.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)


@app.route('/hospital/register', methods=['GET', 'POST'])
def hospital_register():
    if request.method == 'POST':
        name = request.form['name']
        email = request.form['email']
        password = request.form['password']
        location = request.form['location']

        new_hospital = Hospital(
            name=name,
            email=email,
            password=password,
            location=location
        )

        db.session.add(new_hospital)
        db.session.commit()

        return redirect(url_for('hospital_login'))

    return render_template('hospital_register.html')


@app.route('/hospital/login', methods=['GET', 'POST'])
def hospital_login():
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']

        hospital = Hospital.query.filter_by(email=email, password=password).first()

        if hospital:
            session['hospital_id'] = hospital.id
            session['hospital_name'] = hospital.name
            return redirect(url_for('hospital_dashboard'))

        return "Invalid Credentials"

    return render_template('hospital_login.html')


# Home
@app.route('/')
def home():
    return render_template('index.html')



# Register
@app.route('/doctor/register', methods=['GET', 'POST'])
def doctor_register():
    if request.method == 'POST':
        name = request.form['name']
        email = request.form['email']
        password = request.form['password']
        license_number = request.form['license']
        speciality = request.form['speciality']

        new_doctor = Doctor(
            name=name,
            email=email,
            password=password,
            license_number=license_number,
            speciality=speciality
        )

        db.session.add(new_doctor)
        db.session.commit()

        return redirect(url_for('doctor_login'))

    return render_template('doctor_register.html')


# Login
@app.route('/doctor/login', methods=['GET', 'POST'])
def doctor_login():
    if request.method == 'POST':
        email = request.form.get('email')
        password = request.form.get('password')

        doctor = Doctor.query.filter_by(email=email, password=password).first()

        if doctor:
            session['doctor_id'] = doctor.id
            return redirect('/doctor/dashboard')
        else:
            return "Invalid Email or Password"

    return render_template('doctor_login.html')


#docdashboard
@app.route('/doctor/dashboard')
def doctor_dashboard():
    if 'doctor_id' not in session:
        return redirect(url_for('doctor_login'))

    doctor_id = session['doctor_id']

    doctor = Doctor.query.get(doctor_id)

    # Get jobs based on speciality
    jobs = Job.query.filter_by(specialization=doctor.speciality).all()

    # Get applications
    applications = Application.query.filter_by(doctor_id=doctor_id).all()

    stats = {
        "jobs": len(jobs),
        "applications": len(applications)
    }

    return render_template(
        'doctor_dashboard.html',
        doctor=doctor,
        jobs=jobs,
        applications=applications,
        stats=stats
    )

    
# Doctor Model
class Doctor(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100))
    email = db.Column(db.String(100), unique=True)
    password = db.Column(db.String(100))
    license_number = db.Column(db.String(100))
    speciality = db.Column(db.String(100))



    # Doctor Subscription
class DoctorSubscription(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    doctor_id = db.Column(db.Integer)
    plan = db.Column(db.String(50))
    status = db.Column(db.String(50))


class Hospital(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(150))
    email = db.Column(db.String(100), unique=True)
    password = db.Column(db.String(100))
    location = db.Column(db.String(100))


class Job(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(100))
    specialization = db.Column(db.String(100))
    hospital_id = db.Column(db.Integer)
    hospital_name = db.Column(db.String(100))
    location = db.Column(db.String(100))
    salary = db.Column(db.String(50))


class Application(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    doctor_id = db.Column(db.Integer)
    job_id = db.Column(db.Integer)


class Subscription(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    hospital_id = db.Column(db.Integer)
    plan = db.Column(db.String(50))
    status = db.Column(db.String(50))

@app.route('/add-jobs')
def add_jobs():
    job1 = Job(title="Cardiologist", specialization="Cardiology", hospital="Apollo", location="Ahmedabad", salary="₹1,50,000")
    job2 = Job(title="Neurologist", specialization="Neurology", hospital="City Hospital", location="Surat", salary="₹1,20,000")

    db.session.add(job1)
    db.session.add(job2)
    db.session.commit()

    return "Jobs Added!"

@app.route('/doctor/jobs')
def doctor_jobs():
    if 'doctor_id' not in session:
        return redirect('/doctor/login')

    doctor = Doctor.query.get(session['doctor_id'])
    jobs = Job.query.filter_by(specialization=doctor.speciality).all()

    return render_template('doctor_jobs.html', jobs=jobs)

@app.route('/interviews')
def interviews():
    return render_template('interviews.html')


@app.route('/apply/<int:job_id>')
def apply_job(job_id):
    if 'doctor_id' not in session:
        return redirect(url_for('doctor_login'))

    doctor_id = session['doctor_id']

    existing = Application.query.filter_by(job_id=job_id, doctor_id=doctor_id).first()

    if existing:
        return "Already Applied!"

    app_entry = Application(doctor_id=doctor_id, job_id=job_id)
    db.session.add(app_entry)
    db.session.commit()

    return redirect(url_for('doctor_dashboard'))

@app.route('/doctor/subscription', methods=['GET', 'POST'])
def doctor_subscription():
    if 'doctor_id' not in session:
        return redirect(url_for('doctor_login'))

    if request.method == 'POST':
        plan = request.form['plan']

        sub = DoctorSubscription(
            doctor_id=session['doctor_id'],
            plan=plan,
            status="Active"
        )

        db.session.add(sub)
        db.session.commit()

        return "Subscription Activated!"

    return render_template('doctor_subscription.html')

@app.route('/hospital/dashboard')
def hospital_dashboard():
    if 'hospital_id' not in session:
        return redirect(url_for('hospital_login'))

    hospital_id = session['hospital_id']

    jobs = Job.query.filter_by(hospital_id=hospital_id).all()

    applications = db.session.query(Application, Doctor, Job)\
    .join(Job, Job.id == Application.job_id)\
    .join(Doctor, Doctor.id == Application.doctor_id)\
    .filter(Job.hospital_id == hospital_id).all()

    stats = {
        "jobs": len(jobs),
        "applications": len(applications),
        "interviews": 0,
        "notifications": 1
    }

    return render_template(
        "hospital_dashboard.html",
        stats=stats,
        applications=applications
    )


@app.route('/hospital/post-job', methods=['GET', 'POST'])
def post_job():
    if 'hospital_id' not in session:
        return redirect(url_for('hospital_login'))

    if request.method == 'POST':
        job = Job(
            title=request.form['title'],
            specialization=request.form['specialization'],
            hospital_id=session['hospital_id'],
            hospital_name=session['hospital_name'],
            location=request.form['location'],
            salary=request.form['salary']
        )

        db.session.add(job)
        db.session.commit()

        return redirect(url_for('hospital_dashboard'))

    return render_template('post_job.html')


@app.route('/hospital/jobs')
def hospital_jobs():
    if 'hospital_id' not in session:
        return redirect(url_for('hospital_login'))

    jobs = Job.query.filter_by(hospital_id=session['hospital_id']).all()
    return render_template('hospital_jobs.html', jobs=jobs)


@app.route('/hospital/subscription', methods=['GET', 'POST'])
def hospital_subscription():
    if 'hospital_id' not in session:
        return redirect(url_for('hospital_login'))

    if request.method == 'POST':
        plan = request.form['plan']

        sub = Subscription(
            hospital_id=session['hospital_id'],
            plan=plan,
            status="Active"
        )

        db.session.add(sub)
        db.session.commit()

        return "Subscription Activated!"

    return render_template('subscription.html')


@app.route('/hospital/applications')
def hospital_applications():
    if 'hospital_id' not in session:
        return redirect(url_for('hospital_login'))

    applications = db.session.query(Application, Doctor, Job)\
        .join(Job, Job.id == Application.job_id)\
        .join(Doctor, Doctor.id == Application.doctor_id)\
        .filter(Job.hospital_id == session['hospital_id']).all()

    return render_template('hospital_applications.html', applications=applications)


@app.route('/notifications')
def notifications():
    return render_template('notifications.html')

if __name__ == '__main__':
    with app.app_context():
        db.create_all()

    app.run(host='0.0.0.0', port=5000, debug=True)