export default function icons() {
    Array.from(document.querySelectorAll('.basic-icon')).forEach(
        (buttonElement) => {
            fetch(`/images/${buttonElement.textContent}.svg`)
                .then((response) => response.text())
                .then((response) => {
                    // eslint-disable-next-line no-param-reassign
                    buttonElement.innerHTML = response;
                });
        }
    );
}
