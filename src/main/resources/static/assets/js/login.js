class App {
    static DOMAIN = location.origin;

    static BASE_URL_AUTHORIZATION = this.DOMAIN + "/api/auth";

    static ERROR_401 = "Wrong username or password."
    static ERROR_403 = "Account disabled. Please contact admin."
    static ERROR_404 = "Page not found.";
    static ERROR_500 = "Server error. Please contact admin";

    static SweetAlert = class {
        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'center',
                showConfirmButton: false,
                timer: 1500,
            })
        }

        static showErrorAlert(t) {
            Swal.fire({
                icon: 'error',
                title: 'Warning',
                text: t,
                timer: 2000,
            })
        }
    }

    static IziToast = class {
        static showSuccessAlert(t) {
            iziToast.success({
                title: 'Success',
                position: 'topRight',
                timeout: 2500,
                message: t
            });
        }

        static showErrorAlert(t) {
            iziToast.error({
                title: 'Error',
                position: 'topRight',
                timeout: 3500,
                message: t
            });
        }
    }
}

class User {
    constructor(username, password) {
        this.username = username;
        this.password = password;
    }
}