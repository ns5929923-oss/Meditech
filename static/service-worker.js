self.addEventListener("install", event => {
    console.log("SW Installed");
    self.skipWaiting();   // 🔥 force activate
});

self.addEventListener("activate", event => {
    console.log("SW Activated");
    return self.clients.claim();  // 🔥 take control immediately
});