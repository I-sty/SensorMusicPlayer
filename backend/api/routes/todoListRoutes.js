'use strict';
module.exports = function(app) {
  var todoList = require('../controllers/todoListController');

  // todoList Routes
  app.route('/tasks')
    .get(todoList.list_all_tasks)
    .post(todoList.create_a_task);


  app.route('/tasks/:taskId')
    .get(todoList.read_a_task)
    .put(todoList.update_a_task)
    .delete(todoList.delete_a_task);
    
    // buffer routes
  app.route('/buffers')
    .get(todoList.list_all_buffers)
    .post(todoList.create_a_buffer);
    
   app.route('/buffers/:bufferId')
    .get(todoList.read_a_buffer)
    .put(todoList.update_a_buffer)
    .delete(todoList.delete_a_buffer);
};
