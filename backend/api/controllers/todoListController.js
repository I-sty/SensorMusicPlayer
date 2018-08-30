'use strict';

var mongoose = require('mongoose'),
  Buffer = mongoose.model('Buffers');

var PythonShell = require('python-shell');

function callPythonMethod(req, res){
  var options = {
    mode: 'text',
    pythonPath: 'python3',
    pythonOptions: ['-u'], // get print results in real-time
    scriptPath: '/home/isti/StudioProjects/SensorMusicPlayer/backend/python/',
    args: [req]
  };
  PythonShell.run('isti-distance.py', options, function (err, results) {
    if (err) throw err;
    // results is an array consisting of messages collected during execution
    console.log(results);
    res.json({"result": results});
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
      console.log(err);
    console.log(task);
  });
  var stringify = JSON.stringify(req.body);
  var body = JSON.parse(stringify);
  callPythonMethod(body.value.toString(), res);
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
