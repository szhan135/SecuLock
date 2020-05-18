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
const verifyRouter = require('./routes/verify');
// const WorldDataRouter = require('./routes/WorldData');
// const AgeDataRouter = require('./routes/AgeData');
// const GenderDataRouter = require('./routes/GenderData');
// const CaseDataRouter = require('./routes/CaseData');

// App set up

// Used to make server decode data from body of requests
app.use(bodyParser.urlencoded({ extended: true }));

// Public assests set up
app.use(express.static(path.join(__dirname, 'public')));

// Used to output incoming requests in console
app.use(morgan('dev'));

//Used to make server receive requests from client server
app.use(cors());

// Used to recognize incoming requests as Json objects
app.use(express.json());

// Set up routes
app.use('/verify', verifyRouter);
// app.use('/WorldData', WorldDataRouter);
// app.use('/AgeData', AgeDataRouter);
// app.use('/GenderData', GenderDataRouter);
// app.use('/CaseData', CaseDataRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
	next(createError(404));
});

// Start listening
app.listen(port, () => {
	console.log(`Sever is running on port: ${port}`);
});

module.exports = app;
