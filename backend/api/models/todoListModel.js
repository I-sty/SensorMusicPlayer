'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var BufferSchema = new Schema({
  x: [{ type: Number,  required: 'Array is required' }],
  y: [{ type: Number,  required: 'Array is required' }],
  z: [{ type: Number,  required: 'Array is required' }]
});

var ShapeSchema = new Schema({
  x: [{ type: Number,  required: 'Array is required' }],
  y: [{ type: Number,  required: 'Array is required' }],
  z: [{ type: Number,  required: 'Array is required' }],
  shape: {type: String}
});

module.exports = mongoose.model('Buffers', BufferSchema);
module.exports = mongoose.model('Shapes', ShapeSchema);
