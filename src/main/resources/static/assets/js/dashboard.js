class App {
    static DOMAIN = location.origin;

    static BASE_URL_AUTHORIZATION = this.DOMAIN + "/api/auth";
    static BASE_URL_DASHBOARD = this.DOMAIN + "/api/dashboard";
    static BASE_URL_USER = this.DOMAIN + "/api/users";
    static BASE_URL_USER_GROUPS = this.DOMAIN + "/api/users/asgnGrp";
    static BASE_URL_AMZN_ACCOUNT = this.DOMAIN + "/api/amzn-account";
    static BASE_URL_GROUP = this.DOMAIN + "/api/groups";
    static BASE_URL_FILE_UPLOAD = this.DOMAIN + "/api/file-upload";

    static SweetAlert = class {
        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'center',
                showConfirmButton: false,
                timer: 800,
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

        static showChangeStatusDialog(status) {
            return Swal.fire({
                icon: `question`,
                text: `${status ? questionToActive : questionToDisable}`,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#5a6268',
                confirmButtonText: btnAgree,
                cancelButtonText: btnDisagree,
            })
        }

        static showDeleteGroupDialog() {
            return Swal.fire({
                icon: `warning`,
                text: `Are you sure to DELETE this group?`,
                showCancelButton: true,
                confirmButtonColor: '#C21010',
                cancelButtonColor: '#5a6268',
                confirmButtonText: btnAgree,
                cancelButtonText: btnDisagree,
            })
        }

        static showTimeOut(title, text, time, action) {
            let timerInterval;
            return Swal.fire({
                title: title,
                html: `${text}<br>in <b></b> seconds.`,
                timer: time,
                timerProgressBar: true,
                didOpen: () => {
                    Swal.showLoading();
                    const b = Swal.getHtmlContainer().querySelector('b');
                    timerInterval = setInterval(() => {
                        b.textContent = Math.round(Swal.getTimerLeft()/1000)
                    }, 1000)
                },
                willClose: () => {
                    clearInterval(timerInterval)
                }
            }).then((result) => {
                if (result.dismiss === Swal.DismissReason.timer) {
                    action();
                }
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

    static formatNumber() {
        $(".num-space").number(true, 0, ',', ' ');
        $(".num-point").number(true, 0, ',', '.');
        $(".num-comma").number(true, 0, ',', ',');
    }

    static formatNumberSpace(x) {
        if (x == null) {
            return x;
        }
        return x.toString().replace(/ /g, "").replace(/\B(?=(\d{3})+(?!\d))/g, " ");
    }

    static removeFormatNumberSpace(x) {
        if (x == null) {
            return x;
        }
        return x.toString().replace(/ /g, "")
    }

    static formatTooltip() {
        $('[data-toggle="tooltip"]').tooltip();
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

    static handleFailedTasks(jqXHR) {
        switch (jqXHR.status) {
            case 400:
                this.IziToast.showErrorAlert(ERROR_400);
                break;
            case 401:
                this.SweetAlert.showTimeOut(
                    ERROR_401,
                    warningRedirect,
                    4000,
                    this.redirectToLogin(4000));
                break;
            case 403:
                this.SweetAlert.showErrorAlert(ERROR_403);
                break;
            case 404:
                this.IziToast.showErrorAlert(ERROR_404);
                break;
            case 409:
                this.IziToast.showErrorAlert(jqXHR.responseJSON.message);
                break;
            default:
                this.IziToast.showErrorAlert(ERROR_500);
                break;
        }
    }

    static redirectToLogin(timeout) {
        setTimeout(() => {
            location.href = "/logout";
        }, timeout);
    }
}

class User {
    constructor(id, username, fullName, disabled) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.disabled = disabled;
    }
}

class Group {
    constructor(id, title) {
        this.id = id;
        this.title = title;
    }
}