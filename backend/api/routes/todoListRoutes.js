'use strict';
module.exports = function(app) {
  var todoList = require('../controllers/todoListController');

  app.route('/buffers')
    .get(todoList.list_all_buffers)
    .post(todoList.create_a_buffer);
    
   app.route('/buffers/:bufferId')
    .get(todoList.read_a_buffer)
    .put(todoList.update_a_buffer)
    .delete(todoList.delete_a_buffer);
};
