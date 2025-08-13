function validatePasswords() {
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const errorDiv = document.getElementById('passwordError')

    console.log("validate called");

    if(password.value !== confirmPassword.value) {
        confirmPassword.classList.add('is-invalid');
        errorDiv.classList.remove('d-none')
        return false;
    }
    else{
        confirmPassword.classList.remove('is-invalid');
        errorDiv.classList.add('d-none')
        return true;
    }
}