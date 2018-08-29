var PythonShell = require('python-shell');

var options = {
  mode: 'text',
  pythonPath: 'python3',
  pythonOptions: ['-u'], // get print results in real-time
  scriptPath: 'python/',
  args: ['0,1,2,3,4,5,6,7,8']
};

PythonShell.run('isti-distance.py', options, function (err, results) {
  if (err) throw err;
  // results is an array consisting of messages collected during execution
  console.log('results: %j', results);
});
