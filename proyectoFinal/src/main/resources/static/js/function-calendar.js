// Funciones para el calendario y modal:
// Funcion para mostrar el modal con el día seleccionado:
// NOTA: ADAPTAR PARA EL LLAMADO, POR EL MOMENTO FALLA EN EL LLAMADO DESDE EL HTML
function mostrarModal(dia) {
    const modal = document.getElementById("modalCalendario");
    const contenido = document.getElementById("modalContenido");

    contenido.textContent = "Has seleccionado el día: " + dia;

    // Mostrar modal
    modal.classList.remove("opacity-0", "pointer-events-none");
    modal.classList.add("opacity-100");

    // animación scale
    const modalBox = modal.querySelector(".modal-box");
    modalBox.classList.remove("scale-95");
    modalBox.classList.add("scale-100");
}

// Función para cerrar el modal
function cerrarModal() {
    const modal = document.getElementById("modalCalendario");
    const modalBox = modal.querySelector(".modal-box");

    modal.classList.add("opacity-0", "pointer-events-none");
    modal.classList.remove("opacity-100");

    modalBox.classList.add("scale-95");
    modalBox.classList.remove("scale-100");
}

// Funcion para cerrar al hacer clic fuera del modal (backdrop)
document.getElementById("modalCalendario").addEventListener("click" ,function (e) {
    if (e.target === this) {
        cerrarModal();
    }
});