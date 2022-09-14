class LGApp {
    static handleFailedTasks(jqXHR) {
        switch (jqXHR.status) {
            case 401:
                CommonApp.IziToast.showErrorAlert(ERROR_LOGIN_401);
                break;
            case 403:
                CommonApp.IziToast.showErrorAlert(ERROR_LOGIN_403);
                break;
            default:
                CommonApp.IziToast.showErrorAlert(ERROR_LOGIN_500);
                break;
        }
    }
}

class User {
    constructor(username, password) {
        this.username = username;
        this.password = password;
    }
}

(() => {
    CommonApp.loadingbar.handleLoadingBarLG();
})()