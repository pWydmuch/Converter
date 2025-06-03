const apiUrl = import.meta.env.VITE_API_URL;

const form = document.querySelector('.my-form')
const toArabicRadio = document.querySelector('#to-arabic')
const toRomanRadio = document.querySelector('#to-roman')
const enterInput = document.querySelector('#enter-input')
const result = document.querySelector('#result')
const alert = document.querySelector('.alert')
let currentTarget = 'roman-to-arabic'

form.addEventListener('submit', event => {
    event.preventDefault()
    const number = enterInput.value
    fetch(`${apiUrl}/${currentTarget}?number=${number}`)
        .then((response) => {
            if (response.ok) fillResult(response.text())
            else showErrorMessage(response.text())
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
    enterInput.placeholder = 'eg 12'
    alert.style.display = 'none'
    currentTarget = 'arabic-to-roman'
})

toArabicRadio.addEventListener('click', () => {
    result.value = ""
    enterInput.value = ""
    enterInput.focus()
    enterInput.placeholder = 'eg XII'
    alert.style.display = 'none'
    currentTarget = 'roman-to-arabic'
})