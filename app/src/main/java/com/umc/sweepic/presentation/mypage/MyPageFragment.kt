package com.umc.sweepic.presentation.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.umc.sweepic.R
import com.umc.sweepic.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private val mypageViewModel: MypageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation Bar 숨기기
        (activity as? MainActivity)?.hideNavigationBar()

        // Back button
        view.findViewById<ImageView>(R.id.iv_back)?.setOnClickListener {
            findNavController().navigateUp()
        }

        // 사용자 정보 가져오기
        mypageViewModel.fetchUserInfo()

        // 사용자 정보가 변경되면 UI 업데이트
        mypageViewModel.userInfo.observe(viewLifecycleOwner) { user ->
            user?.let {
                view.findViewById<TextView>(R.id.tv_name)?.text = it.name
                view.findViewById<TextView>(R.id.tv_email)?.text = it.email
                view.findViewById<TextView>(R.id.tv_goal)?.text = "${it.goalCount}장"
            }
        }

        // 이름 수정 버튼 클릭 이벤트 (기존 기능 유지)
        view.findViewById<View>(R.id.btn_next_name).setOnClickListener {
            showEditNameDialog()
        }

        // 이름 변경 성공 시 UI 업데이트
        mypageViewModel.nameUpdateStatus.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                val newName = mypageViewModel.updatedName.value ?: return@Observer
                updateName(newName)
                Toast.makeText(requireContext(), "이름이 변경되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "이름 변경 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        })

        // 목표 수정 버튼 클릭 이벤트
        view.findViewById<View>(R.id.btn_next_goal).setOnClickListener {
            showEditGoalDialog()
        }

        // 목표 변경 성공 시 UI 업데이트
        mypageViewModel.goalUpdateStatus.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                val newGoal = mypageViewModel.updatedGoalCount.value ?: return@Observer
                updateGoal(newGoal.toString()) // ✅ UI 업데이트
                Toast.makeText(requireContext(), "목표가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "목표 변경 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        })

        // 로그아웃 버튼 클릭 이벤트
        view.findViewById<View>(R.id.btn_logout).setOnClickListener {
            showLogoutDialog()
        }

        view.findViewById<View>(R.id.btn_withdrawal).setOnClickListener{
            showWithdrawalDialog()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Navigation Bar 다시 표시
        (activity as? MainActivity)?.showNavigationBar()
    }

    private fun showEditNameDialog() {
        // 다이얼로그 레이아웃을 가져옴
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_name, null)
        val editText = dialogView.findViewById<EditText>(R.id.et_input_name)
        val charCountTextView = dialogView.findViewById<TextView>(R.id.tv_char_count)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_name_dialog_cancel)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_name_dialog_confirm)

        // 다이얼로그 빌더 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // 취소 버튼 클릭 이벤트
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // EditText의 입력값 변화를 실시간으로 감지
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentLength = s?.length ?: 0
                charCountTextView.text = "$currentLength/10"
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 확인 버튼 클릭 이벤트
        confirmButton.setOnClickListener {
            val newName = editText.text.toString().trim()
            if (newName.isNotEmpty()) {
                //updateName(newName)
                mypageViewModel.updateUserName(newName) //name update api
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun updateName(newName: String) {
        view?.findViewById<TextView>(R.id.tv_name)?.text = newName
    }

    private fun showEditGoalDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_goal, null)
        val editText = dialogView.findViewById<EditText>(R.id.et_input_goal)
        val warningTextView = dialogView.findViewById<TextView>(R.id.tv_goal_warning)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_goal_dialog_cancel)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_goal_dialog_confirm)

        val decimalFormat = DecimalFormat("#,###")

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // EditText에 TextWatcher 추가
        editText.addTextChangedListener(object : TextWatcher {
            private var currentText: String = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().replace(",", "") // 쉼표 제거
                if (input == currentText) return // 중복 실행 방지

                if (input.isEmpty()) { // 입력값이 비어 있을 때 처리
                    editText.setTextColor(requireContext().getColor(R.color.black))
                    warningTextView.visibility = View.GONE
                    return
                }

                try {
                    val formattedValue = decimalFormat.format(input.toLong()) // 쉼표 추가
                    currentText = formattedValue
                    editText.removeTextChangedListener(this) // 리스너 임시 제거
                    editText.setText(formattedValue)
                    editText.setSelection(formattedValue.length) // 커서를 끝으로 이동
                    editText.addTextChangedListener(this) // 리스너 다시 추가

                    val numericValue = input.toLong()
                    if (numericValue > 10000) {
                        editText.setTextColor(requireContext().getColor(R.color.warning))
                        warningTextView.visibility = View.VISIBLE
                    } else {
                        editText.setTextColor(requireContext().getColor(R.color.black))
                        warningTextView.visibility = View.GONE
                    }
                } catch (e: NumberFormatException) {
                    // 예외 처리: 숫자가 아닌 경우
                    editText.setText("")
                }
            }
        })

        // 취소 버튼 클릭
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // 확인 버튼 클릭
        confirmButton.setOnClickListener {
            val input = editText.text.toString().replace(",", "")
            val numericValue = input.toLongOrNull()
            if (numericValue != null && numericValue <= 10000) {
                //updateGoal(decimalFormat.format(numericValue))
                mypageViewModel.updateGoalCount(numericValue.toInt()) //목표 api
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "올바른 목표 값을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun updateGoal(newGoal: String) {
        val goalTextView = view?.findViewById<TextView>(R.id.tv_goal)
        goalTextView?.text = newGoal + "장"
        Toast.makeText(requireContext(), "목표가 설정되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun showLogoutDialog() {
        // 다이얼로그 레이아웃을 가져옴
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_logout_dialog_cancel)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_logout_dialog_confirm)

        // 다이얼로그 빌더 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // 취소 버튼 클릭 이벤트
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // 확인 버튼 클릭 이벤트
        confirmButton.setOnClickListener {
            dialog.dismiss()
            mypageViewModel.logout()
            performLogout()
        }

        dialog.show()
    }

    private fun showWithdrawalDialog() {
        // 다이얼로그 레이아웃을 가져옴
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_withdrawal, null)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_withdrawal_dialog_cancel)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_withdrawal_dialog_confirm)

        // 다이얼로그 빌더 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // 취소 버튼 클릭 이벤트
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // 확인 버튼 클릭 이벤트
        confirmButton.setOnClickListener {
            dialog.dismiss()
            mypageViewModel.withdrawal()
            performWithdrawal()
        }

        dialog.show()
    }

    private fun performLogout() {
        // 로그아웃 처리 로직
        Toast.makeText(requireContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()

        // 예: 로그인 화면으로 이동
        //findNavController().navigate(R.id.action_myPageFragment_to_loginFragment)
    }

    private fun performWithdrawal() {
        // 로그아웃 처리 로직
        Toast.makeText(requireContext(), "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()

        // 예: 처음 화면으로 이동
        //findNavController().navigate(R.id.action_myPageFragment_to_loginFragment)
    }


}