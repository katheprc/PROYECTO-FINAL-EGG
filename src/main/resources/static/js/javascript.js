const palabras = ["Event", "shooting", "meeting", "filming", "production"];
var listaPalabras = document.getElementById("palabras");

/*setInterval(function () {
    var indc = Math.floor(Math.random()*palabras.length);   
    listaPalabras.textContent = palabras[indc];
}, 2000);*/

/*let currentIndex = 0;

        function cambiarPalabra() {
            listaPalabras.textContent = palabras[currentIndex];
            listaPalabras.style.opacity = 1;

            setTimeout(() => {
                listaPalabras.style.opacity = 0;
                currentIndex = (currentIndex + 1) % palabras.length;
                setTimeout(cambiarPalabra, 1000);
            }, 2000);
        }

        cambiarPalabra();*/

/*const IMAGENESINDEXHEADER = [
    "../static/img-carrusel-index-h/imagen-1.jpg"
];

let posicionActual = 0;
let $imagen = document.querySelector('.primary-header');
let intervalo;

function pasarFoto() {
    if (posicionActual >= IMAGENESINDEXHEADER.length - 1) {
        posicionActual = 0;
    } else {
        posicionActual++;
    }
    renderizarImagen();
}

function renderizarImagen() {
    $imagen.style.backgroundImage = `url(${IMAGENESINDEXHEADER[posicionActual]})`;
}

function playIntervalo() {
    intervalo = setInterval(pasarFoto, 3000);
}

renderizarImagen();
playIntervalo();*/
