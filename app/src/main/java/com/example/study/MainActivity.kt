package com.example.study

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.study.todo.Todo
import com.example.study.todo.TodoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var mTodoViewModel: TodoViewModel

    // array
    val data = mutableListOf<String>()

    // data[0] , data[1], ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTodoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)


        button.isEnabled = false

        button.setOnClickListener {
            if (editText.text.toString().isNotEmpty()) {
//                data.add(editText.text.toString())

                // DB
                insertDataToDatabase()

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)

                editText.clearFocus()
                editText.text = null

                Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        editText.addTextChangedListener(editTextWatcher)

        list.adapter = customAdapter

        mTodoViewModel.readAllData.observe(this, Observer { todo ->
            customAdapter.setData(todo)
        })
    }


    // custom adapter
    private val customAdapter = object : BaseAdapter() {

        private var todoList = emptyList<Todo>()

        fun setData(user: List<Todo>){
            this.todoList = user
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return todoList.size
        }

        override fun getItem(position: Int): Any {
            return todoList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var newRowView = convertView

            if (newRowView == null) {
                newRowView = layoutInflater.inflate(R.layout.row, null)
            }

            newRowView?.run {
                editTextView.text = todoList[position].content

                // 버튼에 index 부여
                editTextEditButton.tag = position
                editTextDeleteButton.tag = position

                editTextEditButton.setOnClickListener {
                    selectedItem.text = "$position 번째  edit 버튼이 눌렸습니다."
                    viewMode.isGone = true
                    editMode.isGone = false
                }

                editTextDeleteButton.setOnClickListener {
                    selectedItem.text = "$position 번째 delete 버튼이 눌렸습니다."
                    data.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(applicationContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }

                editModeSubmitButton.setOnClickListener {
                    // init data
                    editModeTextView.setText(data[position])

                    if (editModeTextView.text.toString().isNotEmpty()) {
                        // save edited data
                        data[position] = editModeTextView.text.toString()
                        notifyDataSetChanged()

                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(editModeTextView.windowToken, 0)

                        editModeTextView.clearFocus()

                        Toast.makeText(applicationContext, "수정되었습니다.", Toast.LENGTH_SHORT).show()

                        viewMode.isGone = false
                        editMode.isGone = true
                    }
                }

            }

            return newRowView!!
        }
    }

    val editTextWatcher = object : TextWatcher {
        // 문자열이 변경 되기 전
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        // 문자열 변경 작업이 완료 되었을 때
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.length!! > 0) {
                button.isEnabled = true
            } else if (s?.length!! == 0) {
                button.isEnabled = false
            }
        }

        // 변경 된 문자열이 화면에 반영 되었을
        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun insertDataToDatabase(): Boolean {
        val content = editText.text.toString()
        return if (inputCheck(content)) {
            val todo = Todo(0, content, false)
            mTodoViewModel.addTodo(todo)
            true
        } else {
            false
        }
    }

    private fun inputCheck(content: String): Boolean {
        return !(TextUtils.isEmpty(content))
    }
}