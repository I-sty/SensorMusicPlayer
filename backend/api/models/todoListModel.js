'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var BufferSchema = new Schema({
  value: [{ type: Number,  required: 'Array is required' }]
});

module.exports = mongoose.model('Buffers', BufferSchema);
