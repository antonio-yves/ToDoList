package br.com.antonio.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.antonio.todolist.R
import br.com.antonio.todolist.databinding.ItemTaskBinding
import br.com.antonio.todolist.model.Task

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()){

    var listenerEdit : (Task) -> Unit = {}
    var listenerDelete : (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.taskTitle.text = item.title
            binding.taskData.text = "${item.date} ${item.hour}"
            binding.taskOptions.setOnClickListener {
                showPopup(item)
            }
        }

        private fun showPopup(item: Task) {
            val taskOptions = binding.taskOptions
            val popupMenu = PopupMenu(taskOptions.context, taskOptions)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId){
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> listenerDelete(item)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }

    }
}

class  DiffCallback : DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
}