function visibility(btnAction, btnNewAction, type) {
    btnAction.classList.add('hidden');
    btnNewAction.classList.remove('hidden');
    document.querySelector('#password').setAttribute('type', type);
}

window.addEventListener(
    'load',
    () => {
        const btnPassVisibleOn = document.querySelector('.visibility-on');
        const btnPassVisibleOff = document.querySelector('.visibility-off');

        btnPassVisibleOn.addEventListener('click', () => {
            visibility(btnPassVisibleOn, btnPassVisibleOff, 'password');
        });
        btnPassVisibleOff.addEventListener('click', () => {
            visibility(btnPassVisibleOff, btnPassVisibleOn, 'text');
        });

        // document.querySelector('#login').addEventListener('click', () => {
        //     const email = document.querySelector('#email').value;
        //     const password = document.querySelector('#password').value;

        //     const formData = new FormData();
        //     formData.append('username', email);
        //     formData.append('password', password);

        //     const request = new XMLHttpRequest();
        //     request.open("POST", "/login");
        //     request.send(formData);
        // });
    },
    false
);
