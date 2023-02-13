let headPanelInfo = (user) => {
    document.getElementById('headPanel').innerHTML =
        `<h5><strong>${user.username}</strong> with roles: ${user.roles.map(role => ` `+ role.name.substring(5))}</h5>`
}

const showNameUserInfo = (user) => {
    const contName = document.querySelector('.card-header')
    contName.innerHTML +=
        `<h5>Welcom to ${user.username} page</h5>`
}

const showUserInfo = (user) => {
    const container = document.querySelector('.tbodyUser')
    container.innerHTML +=
        `<tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(role => ` `+ role.name.substring(5))}</td>
        </tr>`
}


fetch("/api/user/")
    .then(response => response.json())
    .then(data => {
        headPanelInfo(data);
        showUserInfo(data);
    showNameUserInfo(data);
    })
    .catch(error => console.error(`Error: `,error))
