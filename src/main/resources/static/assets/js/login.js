class App {
    static DOMAIN = location.origin;

    static BASE_URL_AUTHORIZATION = this.DOMAIN + "/api/auth";
    static BASE_URL_DASHBOARD = this.DOMAIN + "/api/dashboard";

    static ERROR_400 = "Task failed, please check your data.";
    static ERROR_401 = "Access timeout. Redirecting to login."
    static ERROR_403 = "Access denied. Unauthorized personnel cannot perform this action.";
    static ERROR_404 = "An error occurred. Please try again later!";
    static ERROR_500 = "Task failed successfully, please contact administrators.";

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