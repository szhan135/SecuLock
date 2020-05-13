const express = require('express');
const createError = require('http-errors');
const path = require('path');
const cors = require('cors');
const app = express();
const morgan = require('morgan');
let bodyParser = require('body-parser');

// Port to listen to
const port = 5000;

// Import routers
// const verifyRouter = require('./routes/verify');


// App set up
app.get("/", (req, res) => {
	console.log("Responding to root route")
	res.send("Welcome to SecuLock")
  })

  app.get("/users", (req,res) => {
	var user1 = {firstName: "Stephen", lastName: "Curry", id: 1 }
	const user2 = {firstName: "Kally", lastName: "Durant", id: 2}
	//res.send("Known Users Allow Access")
	res.json([user1,user2])
  })

  app.get("/verify", (req,res) => {
	var user1 = {firstName: "Stephen", lastName: "Curry", id: "1"}
	const user2 = {firstName: "Kally", lastName: "Durant", id: '2'}
	res.send("Known Users Allow Access")
	res.json([user1,user2])
  })
// // Used to make server decode data from body of requests

// // Used to output incoming requests in console
// app.use(morgan('dev'));

// //Used to make server receive requests from client server
// app.use(cors());

// // Used to recognize incoming requests as Json objects
// app.use(express.json());

// const verifyRouter = require('./routes/verify');
// app.use('/verify', verifyRouter);
// // catch 404 and forward to error handler
// app.use(function (req, res, next) {
// 	next(createError(404));
// });

// Start listening
app.listen(port, () => {
	console.log(`Sever is running on port: ${port}`);
});

module.exports = app;
