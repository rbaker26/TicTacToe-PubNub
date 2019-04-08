const express = require('express')
const app = express()
 
app.get('/', function (req, res) {
  res.json({ message: 'Hello World api!' });  
})
 
app.listen(3000, function () {
  console.log('Three Thousand app run in port 3000!')
})