const norm = p => {
    if (!p) return '/';
    let out = p.split('?')[0].split('#')[0];
    if (out.length > 1 && out.endsWith('/')) out = out.slice(0, -1);
    return out || '/';
};

const current = norm(window.location.pathname);

const links = document.querySelectorAll('#navbarNav .nav-link');
let bestLink = null;
let bestLen = -1;

links.forEach(a => {
    const href = a.getAttribute('href');
    if (!href) return;

    let path;
    try {
        const url = new URL(href, window.location.origin);
        path = norm(url.pathname);
    } catch { return; }

    if (path === current && path.length > bestLen) {
        bestLink = a; bestLen = path.length; return;
    }
    if (path !== '/' && current.startsWith(path) && path.length > bestLen) {
        bestLink = a; bestLen = path.length;
    }
});

if (bestLink) {
    const li = bestLink.closest('.nav-item');
    if (li) li.classList.add('active-link');
    bestLink.classList.add('active');
}
