const form = document.querySelector('.my-form')
const toArabicRadio = document.querySelector('#to-arabic')
const toRomanRadio = document.querySelector('#to-roman')
const last5Checkbox = document.querySelector('#last-5')
const enterInput = document.querySelector('#enter-input')
const result = document.querySelector('#result')
const alert = document.querySelector('.alert')
const recentList = document.querySelector('#recent-list')
let currentTarget = 'arabic-equiv'

form.addEventListener('submit', event => {
    event.preventDefault()
    const number = enterInput.value
    fetch(`http://localhost:8080/${currentTarget}?number=${number}`)
        .then((response) => {
            if (response.ok) fillResult(response.text())
            else showErrorMessage(response.text())
            if (last5Checkbox.checked) {
                fetchRecents();
            }
        })
})

const fillResult = responsePromise => {
    alert.style.display = 'none'
    responsePromise.then(resp => {
        result.value = resp
    })
}

const showErrorMessage = responsePromise => {
    responsePromise.then(resp => {
        alert.style.display = 'block'
        alert.innerText = resp
    })
}

toRomanRadio.addEventListener('click', () => {
    result.value = ""
    enterInput.value = ""
    enterInput.focus()
    alert.style.display = 'none'
    currentTarget = 'roman-equiv'
})

toArabicRadio.addEventListener('click', () => {
    result.value = ""
    enterInput.value = ""
    enterInput.focus()
    alert.style.display = 'none'
    currentTarget = 'arabic-equiv'
})

last5Checkbox.addEventListener('click', e => {
    if (e.target.checked) {
        fetchRecents();

    } else {
        recentList.innerHTML = ''
    }
})

function fetchRecents() {
    fetch(`http://localhost:8080/recents`)
        .then((response) => {
            return response.json()
        })
        .then(json => {
            recentList.innerHTML = ''
            for (const [key, value] of json) {
                const li = document.createElement('li');
                li.textContent = `${key} ⟶ ${value}`;
                recentList.appendChild(li);
            }
            console.log(json)
        })
}
