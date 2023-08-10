<!--  offcanvas  -->
document.addEventListener("DOMContentLoaded", function () {
    var offcanvasElement = document.querySelector("#offcanvasScrolling");
    var offcanvas = new bootstrap.Offcanvas(offcanvasElement);

    var btn = document.querySelector('[data-bs-toggle="offcanvas"]');
    btn.addEventListener("click", function () {
        offcanvas.show();
    });
});




const lists = document.querySelectorAll('.list');
const addListButton = document.getElementById('addList');
let draggedItem = null;

lists.forEach(list => {
    list.addEventListener('dragstart', e => {
        draggedItem = e.target;
        setTimeout(() => {
            e.target.style.opacity = '0.5';
        }, 0);
    });

    list.addEventListener('dragend', e => {
        setTimeout(() => {
            e.target.style.opacity = '1';
            draggedItem = null;
        }, 0);
    });

    list.addEventListener('dragover', e => {
        e.preventDefault();
    });

    list.addEventListener('dragenter', e => {
        e.preventDefault();
        list.classList.add('highlight');
    });

    list.addEventListener('dragleave', e => {
        list.classList.remove('highlight');
    });

    list.addEventListener('drop', e => {
        if (draggedItem) {
            const container = document.getElementById('listContainer');
            if (draggedItem.classList.contains('card')) {
                if (list.contains(draggedItem)) {
                    return;
                }
                list.appendChild(draggedItem);
            } else if (draggedItem.classList.contains('list')) {
                if (draggedItem.contains(list)) {
                    return;
                }
                if (list.nextSibling === draggedItem) {
                    container.insertBefore(draggedItem, list);
                } else {
                    container.insertBefore(draggedItem, list.nextSibling);
                }
            }
            list.classList.remove('highlight');
        }
    });
});

addListButton.addEventListener('click', () => {
    const container = document.getElementById('listContainer');
    const newList = document.createElement('div');
    newList.className = 'list';
    newList.draggable = true;
    newList.innerHTML = `
    <div class="list-header">New List</div>
    <div class="card" draggable="true">New Card</div>
    `;

    newList.addEventListener('dragstart', e => {
        draggedItem = e.target;
        setTimeout(() => {
            e.target.style.opacity = '0.5';
        }, 0);
    });

    newList.addEventListener('dragend', e => {
        setTimeout(() => {
            e.target.style.opacity = '1';
            draggedItem = null;
        }, 0);
    });

    newList.addEventListener('dragover', e => {
        e.preventDefault();
    });

    newList.addEventListener('dragenter', e => {
        e.preventDefault();
        newList.classList.add('highlight');
    });

    newList.addEventListener('dragleave', e => {
        newList.classList.remove('highlight');
    });

    newList.addEventListener('drop', e => {
        if (draggedItem) {
            const container = document.getElementById('listContainer');
            if (draggedItem.classList.contains('card')) {
                if (newList.contains(draggedItem)) {
                    return;
                }
                newList.appendChild(draggedItem);
            } else if (draggedItem.classList.contains('list')) {
                if (newList.contains(draggedItem)) {
                    return;
                }
                if (newList.nextSibling === draggedItem) {
                    container.insertBefore(draggedItem, newList);
                } else {
                    container.insertBefore(draggedItem, newList.nextSibling);
                }
            }
            newList.classList.remove('highlight');
        }
    });

    container.appendChild(newList);
});