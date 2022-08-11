class App {
    static DOMAIN = location.origin;

    static BASE_URL_AUTHORIZATION = this.DOMAIN + "/api/auth";
    static BASE_URL_DASHBOARD = this.DOMAIN + "/api/dashboard";
    static BASE_URL_FILE_UPLOAD = this.DOMAIN + "/api/file-upload";

    static ERROR_400 = "Task failed, please check your data.";
    static ERROR_401 = "Access timeout. Redirecting to login."
    static ERROR_403 = "Access denied. Unauthorized personnel cannot perform this action.";
    static ERROR_404 = "An error occurred. Please try again later!";
    static ERROR_500 = "Task failed successfully, please contact administrators.";
    static SUCCESS_CREATED = "Data created successfully!";
    static SUCCESS_UPDATED = "Data updated successfully!";

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

        static showDisableConfirmDialog() {
            return Swal.fire({
                icon: 'warning',
                text: 'Are you sure to disable this user?',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#5a6268',
                confirmButtonText: 'Yes',
                cancelButtonText: 'No',
            })
        }

        static showActivateConfirmDialog() {
            return Swal.fire({
                icon: 'warning',
                text: 'Are you sure to activate this user?',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#5a6268',
                confirmButtonText: 'Yes',
                cancelButtonText: 'No',
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
}

class User {
    constructor(id, username, password, email, address, phone, role, isBlocked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.role = role;
        this.isBlocked = isBlocked;
    }
}

class Role{
    constructor(id, code, name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}

class Product {
    constructor(id, name, price, quantity, isLocked, category, createdBy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isLocked = isLocked;
        this.category = category;
        this.createdBy = createdBy;
    }
}

class Category {
    constructor(id, name) {
        this.id = id;
        this.name = name;
    }
}