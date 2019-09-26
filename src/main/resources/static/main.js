const arabForm = document.querySelector('.to-arabic')

arabForm.addEventListener('submit',event => {
    event.preventDefault()
    fetch("http://localhost:9090/arabska/I")
        .then(resp => resp.json())
        .then(resp => {
            console.log(resp)
        })
        .catch(err =>{
            console.log(err)
        })
})

