package com.umc.sweepic.presentation.mypage

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.google.android.material.materialswitch.MaterialSwitch
import com.umc.sweepic.R
import com.umc.sweepic.presentation.MainActivity
import com.umc.sweepic.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat


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

        // 탈퇴 버튼 클릭 이벤트
        view.findViewById<View>(R.id.btn_withdrawal).setOnClickListener{
            showWithdrawalDialog()
        }

        // 로그아웃 상태 감지
        mypageViewModel.logoutStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                Log.d("MyPageFragment", "로그아웃 성공")
                navigateToLogin()
            } else {
                Toast.makeText(requireContext(), "로그아웃 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        val switchAllNotifications = view.findViewById<MaterialSwitch>(R.id.switch_all_notifications)
        val switchRealtimePhoto = view.findViewById<MaterialSwitch>(R.id.switch_realtime_photo)
        val switchChallengeRecommendation = view.findViewById<MaterialSwitch>(R.id.switch_challenge_recommendation)

        // 전체 알림 스위치 클릭 이벤트
        switchAllNotifications.setOnCheckedChangeListener { _, isChecked ->
            switchRealtimePhoto.isChecked = isChecked
            switchChallengeRecommendation.isChecked = isChecked

            if (isChecked) {
                checkAndEnableNotifications()
            }
        }

        // 개별 알림 스위치 클릭 시 전체 알림 스위치 상태 업데이트
        val updateAllSwitchState = {
            switchAllNotifications.isChecked = switchRealtimePhoto.isChecked && switchChallengeRecommendation.isChecked
        }

        switchRealtimePhoto.setOnCheckedChangeListener { _, _ -> updateAllSwitchState() }
        switchChallengeRecommendation.setOnCheckedChangeListener { _, _ -> updateAllSwitchState() }

    }
    // 기기에서 알림이 허용되어 있는지 확인하고, 허용되지 않았다면 설정 화면으로 이동
    private fun checkAndEnableNotifications() {
        val notificationManager = NotificationManagerCompat.from(requireContext())

        if (!notificationManager.areNotificationsEnabled()) {
            // 알림이 비활성화된 경우, 설정 화면으로 이동 안내
            Toast.makeText(requireContext(), "알림이 비활성화되어 있습니다. 설정에서 활성화해주세요.", Toast.LENGTH_LONG)
                .show()

            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            }
            startActivity(intent)
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
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_goal, null)
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

                    val numericValue: Int? = input.toIntOrNull()

                    if (numericValue == null) {
                        warningTextView.visibility = View.VISIBLE
                        warningTextView.text = "숫자만 입력할 수 있습니다."
                        return
                    }

                    when {
                        numericValue % 100 != 0 -> { // ✅ 100 단위 입력 오류
                            editText.setTextColor(requireContext().getColor(R.color.warning))
                            warningTextView.visibility = View.VISIBLE
                            warningTextView.text = "목표값은 100 단위로 입력해야 합니다."
                        }
                        numericValue >= getCurrentPhotoCount() -> { // ✅ 현재 사진 개수보다 크거나 같음
                            editText.setTextColor(requireContext().getColor(R.color.warning))
                            warningTextView.visibility = View.VISIBLE
                            warningTextView.text = "목표값은 현재 사진 개수보다 작아야 합니다."
                        }
                        else -> { // ✅ 정상 입력값
                            editText.setTextColor(requireContext().getColor(R.color.black))
                            warningTextView.visibility = View.GONE
                            warningTextView.text = ""
                        }
                    }
                } catch (e: NumberFormatException) {
                    // 예외 처리: 숫자가 아닌 경우
                    editText.setText("")
                    warningTextView.visibility = View.VISIBLE
                    warningTextView.text = "숫자만 입력할 수 있습니다."
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
            val numericValue: Int? = input.toIntOrNull()
            val currentPhotoCount = getCurrentPhotoCount() // 📸 현재 기기의 사진 개수 가져오기

            if (numericValue == null) {
                return@setOnClickListener
            }

            if (numericValue % 100 != 0) {
                return@setOnClickListener
            }

            if (numericValue >= currentPhotoCount) {
                return@setOnClickListener
            }

            // 목표 업데이트 API 호출 (numericValue는 null이 아님)
            mypageViewModel.updateGoalCount(numericValue) // ✅ 오류 해결

            dialog.dismiss()
        }
        dialog.show()
    }

        private fun getCurrentPhotoCount(): Int {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )

        val count = cursor?.count ?: 0
        cursor?.close()
        return count
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
            checkSessionId()
            mypageViewModel.logout()

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

    private fun performWithdrawal() {
        // 탈퇴 처리 로직
        Toast.makeText(requireContext(), "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()

        // 로그인 화면으로 이동 (또는 원하는 화면)
        val intent = Intent(requireContext(), LoginActivity::class.java) // 이동할 액티비티 지정
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 백 스택 제거
        startActivity(intent)
    }

    private fun checkSessionId() {
        val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val sessionId = sharedPreferences.getString("SESSION_ID", "null")
        Log.d("MyPageFragment", "현재 세션 ID: $sessionId")
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


}