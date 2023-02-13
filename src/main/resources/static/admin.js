const url = '/api/users';
const arrayRoles = [{id: 1, name: "ROLE_USER"}, {id: 2, name: "ROLE_ADMIN"}];
let idForm = 0;

fetch(url)
    .then(response => response.json())
    .then(data => showUsersInfo(data))
    .catch(error => console.error(error))

let usersInfo = '';
const showUsersInfo = (users) => {
    const container = document.getElementById("tbody-allUsers");
    const arr = Array.from(users);
    arr.forEach(user => {
        usersInfo += `
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(role => ` `+ role.name.substring(5))}</td>
            <td class="text text-white">
                <a class="btnEdit btn btn-info">Edit</a>
            </td>
            <td class="text text-white">
                <a class="btnDelete btn btn-danger" id="deleteUser">Delete</a>
            </td>
        </tr>        `
    })
    container.innerHTML = usersInfo
}

// New User Panel
const newFormUser = document.getElementById('newFormUser');
const userName = document.getElementById('username');
const age = document.getElementById('age');
const email = document.getElementById('email');
const password = document.getElementById('password');
const roles = document.getElementById('newUserRoles');
let option = '';
newUsersTab.addEventListener('click', () => {
    console.log("click button NewUser")
    userName.value = '';
    age.value = '';
    email.value = '';
    password.value = '';
    roles.innerHTML = '';
    arrayRoles.forEach(role => {
            roles.innerHTML += `
            <div class="form-check form-check-inline col-form-label-sm">
              <input class="form-check-input" name="mycheckboxes" type="checkbox" id="inlineCheckbox${role.id}" value="${role.id}">
              <label class="form-check-label" for="inlineCheckbox">${role.name.substring(5)}</label>
            </div>     `
        })
        option = 'newUser';
    })

newFormUser.addEventListener('submit', (e) => {
    e.preventDefault();
    let arrRole = roleArray($('input[name=mycheckboxes]:checked'));
    console.log( arrRole);
    fetch(url, {
        method: 'POST',
        headers:{
            'Content-type': 'application/json'
        },
        body: JSON.stringify({
            username: userName.value,
            age: age.value,
            email: email.value,
            password: password.value,
            roles: arrRole
        })
    })
        .then(response => response.json())
        .then(data => showUsersInfo(data))
        .catch(error => console.log(error))
        .then(reloadShowUsers)
    $('button[href="#adminTable"]').tab('show');
})

const onForm = (element, event , selector, handler) => {
    element.addEventListener(event,f => {
        if(f.target.closest(selector)) {
            console.log('Edit or Delete button on');
            handler(f);
        }
    })
}

//Delete User Modal

const modalUserDelete = new bootstrap.Modal(document.getElementById('modalDelete'));
const userDeleteForm = document.getElementById('modalDelete');
// const idDelete = $('#idDeleteUser');
// console.log(idDelete)
const idDelete = document.getElementById('idDeleteUser');
const userNameDelete = document.getElementById('userNameDelete');
const ageDelete = document.getElementById('deleteUserAge');
const emailDelete = document.getElementById('deleteUserEmail');
const rolesDelete = document.getElementById('rolesDelete');

onForm(document,'click', '.btnDelete', e => {
    const row = e.target.parentNode.parentNode;
    idForm = row.firstElementChild.innerHTML;
    fetch(url +'/'+idForm, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => getUserById(data))
        .catch(error => console.log(error))

    const getUserById = (user) => {
        console.log(user.id)
        idDelete.value = user.id;
        userNameDelete.value = user.username;
        ageDelete.value = user.age;
        emailDelete.value = user.email;
        rolesDelete.value = user.roles.map(role => ` `+ role.name.substring(5));
              }
    modalUserDelete.show();
})

userDeleteForm.addEventListener('submit', (e) => {
    e.preventDefault();
    fetch(url+'/'+idForm, {
        method: 'DELETE'
    })
        .then(data => showUsersInfo(data))
        .catch(error => console.log(error))
        .then(reloadShowUsers)
    modalUserDelete.hide();
})

//Edit Modal User

const formUserEdit = new bootstrap.Modal(document.getElementById('modalEdit'));
const userEditForm = document.getElementById('modalEdit');
const idEdit = document.getElementById('idEditUser');
const userNameEdit = document.getElementById('editUsername');
const ageEdit = document.getElementById('editAge');
const emailEdit = document.getElementById('editUserEmail');
const passwordEdit = document.getElementById('editUserPassword')
const rolesEdit = document.getElementById('editUserRoles');

onForm(document,'click', '.btnEdit', (e) => {
    const row = e.target.parentNode.parentNode;
    idForm = row.firstElementChild.innerHTML;
    fetch(url +'/'+idForm, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => getUserById(data))
        .catch(error => console.log(error))

    const getUserById = (user) => {
        idEdit.value = user.id;
        userNameEdit.value = user.username;
        ageEdit.value = user.age;
        emailEdit.value = user.email;
        passwordEdit.value = '';
        rolesEdit.innerHTML = '';
        arrayRoles.forEach(role => {
            rolesEdit.innerHTML += `
            <div class="form-check form-check-inline col-form-label-sm">
              <input class="form-check-input" name="editCheckboxes" type="checkbox" id="editCheckbox${role.id}" value="${role.id}">
              <label class="form-check-label" for="editCheckbox${role.id}">${role.name.substring(5)}</label>
               </div>`

        }
        )
        // Bad practice, no idea
        let check1 = document.getElementById('editCheckbox1')
        let check2 = document.getElementById('editCheckbox2')
        user.roles.forEach(role => {
            if(+role.id === +check1.value) {
                check1.checked = true;
            }else if(+role.id === +check2.value) {
                check2.checked = true;
            }
        })
    }
    formUserEdit.show();
})

userEditForm.addEventListener('submit', e => {
    e.preventDefault();
    let arrRole = roleArray($('input[name=editCheckboxes]:checked'));
    fetch(url+'/'+idForm, {
        method: 'PATCH',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify({
            id: idForm,
            username: userNameEdit.value,
            age: ageEdit.value,
            email: emailEdit.value,
            password: passwordEdit.value,
            roles: arrRole
        })
    })
        .then(response => response.json())
        .then(data => showUsersInfo(data))
        .catch(error => console.log(error))
        .then(reloadShowUsers)
    formUserEdit.hide();
})


// Util method:


const reloadShowUsers = () => {
    fetch(url)
        .then(response => response.json())
        .then(data => {
            usersInfo = ''
            showUsersInfo(data)
        })
}
let roleArray = (options) => {
    let array = []
    for (let i = 0; i < options.length; i++) {
        if (options[i].checked) {
            console.log(options[i].value)
            let role = {id: +options[i].value}
            array.push(role)
        }
    }
    return array
}
