'use strict';

var mongoose = require('mongoose'),
  Buffer = mongoose.model('Buffers'),
  Task = mongoose.model('Tasks');

// Tasks
exports.list_all_tasks = function(req, res) {
  Task.find({}, function(err, task) {
    if (err)
      res.send(err);
    res.json(task);
  });
};

exports.create_a_task = function(req, res) {
  var new_task = new Task(req.body);
  new_task.save(function(err, task) {
    if (err)
      res.send(err);
    res.json(task);
  });
};

exports.read_a_task = function(req, res) {
  Task.findById(req.params.taskId, function(err, task) {
    if (err)
      res.send(err);
    res.json(task);
  });
};

exports.update_a_task = function(req, res) {
  Task.findOneAndUpdate({_id: req.params.taskId}, req.body, {new: true}, function(err, task) {
    if (err)
      res.send(err);
    res.json(task);
  });
};

exports.delete_a_task = function(req, res) {
  
  Task.remove({
    _id: req.params.taskId
  }, function(err, task) {
    if (err)
      res.send(err);
    res.json({ message: 'Task successfully deleted' });
  });
};

// Buffer
exports.list_all_buffers = function(req, res) {
  Buffer.find({}, function(err, task) {
    if (err)
      res.send(err);
    res.json(task);
  });
};

exports.create_a_buffer = function(req, res) {
  var new_buffer = new Buffer(req.body);
  new_buffer.save(function(err, task) {
    if (err)
      res.send(err);
    res.json(task);
  });
};

exports.read_a_buffer = function(req, res) {
  
  Buffer.findById(req.params.bufferId, function(err, task) {
    if (err)
      res.send(err);
    res.json(task);
  });
};

exports.update_a_buffer = function(req, res) {
  Buffer.findOneAndUpdate({_id: req.params.bufferId}, req.body, {new: true}, function(err, task) {
    if (err)
      res.send(err);
    res.json(task);
  });
};

exports.delete_a_buffer = function(req, res) {
  
  Buffer.remove({
    _id: req.params.bufferId
  }, function(err, task) {
    if (err)
      res.send(err);
    res.json({ message: 'Buffer successfully deleted' });
  });
};
