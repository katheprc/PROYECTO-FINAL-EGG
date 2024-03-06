window.addEventListener("scroll", function () {
    var header = document.querySelector("header");
    header.classList.toggle("abajo", window.scrollY > 0);
})

/***** NavBar Responsive *****/
const sideMenu = document.getElementById("menu");
const menuIcon = document.getElementById("show-menu");

function showMenu() {
    if (window.innerWidth <= 767) {
        sideMenu.classList.add('open');
        sideMenu.style.display = 'flex';
    }
}

function hideMenu() {
    if (window.innerWidth <= 767) {
        sideMenu.classList.remove('open');
        sideMenu.style.display = 'none';
    }
}

function toggleMenu() {
    if (window.innerWidth <= 767) {
        sideMenu.classList.toggle('open');
        if (sideMenu.classList.contains('open')) {
            sideMenu.style.display = 'flex';
        } else {
            hideMenu();
        }
    } else {
        sideMenu.classList.add('open');
        sideMenu.style.display = 'flex';
    }
}

menuIcon.addEventListener('click', toggleMenu);
sideMenu.addEventListener('mouseenter', showMenu);
sideMenu.addEventListener('mouseleave', hideMenu);

const menuLinks = document.querySelectorAll('#menu a.navLink');
menuLinks.forEach(link => {
    link.addEventListener('click', () => {
        hideMenu();
    });
});

window.addEventListener('resize', function () {
    if (window.innerWidth > 767) {
        sideMenu.classList.add('open');
        sideMenu.style.display = 'flex';
    }
});