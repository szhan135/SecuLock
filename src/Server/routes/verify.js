var express = require('express');
var router = express.Router();

const loadUsers = require('../routes/LoadUsers.js');

var knownUsers = loadUsers.loadUser();