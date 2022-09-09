class App {
    static DOMAIN = location.origin;

    static BASE_URL_AUTHORIZATION = this.DOMAIN + "/api/auth";

    static SweetAlert = class {
        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'center',
                showConfirmButton: false,
                timer: 700,
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

    static handleFailedTasks(jqXHR) {
        switch (jqXHR.status) {
            case 401:
                App.IziToast.showErrorAlert(ERROR_LOGIN_401);
                break;
            case 403:
                App.IziToast.showErrorAlert(ERROR_LOGIN_403);
                break;
            default:
                App.IziToast.showErrorAlert(ERROR_LOGIN_500);
                break;
        }
    }

    static allowShowingPassword(form) {
        let password = form.find('.password').get(0);
        let checkbox = form.find('.show-password');

        password.type = 'password';

        checkbox.on('change', function () {
            if (checkbox.is(':checked')) {
                password.type = 'text';
            } else {
                password.type = 'password';
            }
        })
    }
}

class User {
    constructor(username, password) {
        this.username = username;
        this.password = password;
    }
}