const form = document.querySelector('.my-form')
const toArabicRadio = document.querySelector('#to-arabic')
const toRomanRadio = document.querySelector('#to-roman')
const enterInput = document.querySelector('#enter-input')
const result = document.querySelector('#result')
let currentDestination = 'arabska'

form.addEventListener('submit',event => {
    event.preventDefault()
    const number = enterInput.value
//    const dest = currentDestination
    console.log(`http://localhost:9090/${currentDestination}/${number}`)
    fetch(`http://localhost:9090/${currentDestination}/${number}`)
        .then(resp => resp.text())
        .then(resp => {
        console.log(resp)

            result.value=resp
        })
        .catch(err =>{
            console.log(err)
        })
})

toRomanRadio.addEventListener('click',()=>{
console.log('switch to roman')
currentDestination='rzymska'
})
toArabicRadio.addEventListener('click',()=>{

console.log('switch to arab')
currentDestination='arabska'
})



