package com.lion.a066_ex_animalmanager.fragment

import FragmentName
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a066ex_animalmanager.repository.AnimalRepository
import com.lion.a066_ex_animalmanager.MainActivity
import com.lion.a066_ex_animalmanager.R
import com.lion.a066_ex_animalmanager.databinding.FragmentShowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//import com.lion.a066_ex_animalmanager.util.FragmentName


class ShowFragment : Fragment() {

    lateinit var fragmentShowBinding: FragmentShowBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentShowBinding = FragmentShowBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바 설정 메서드 호출
        settingToolbar()
        // 데이터를 가져와 출력한다.
        settingTextView()

        return fragmentShowBinding.root
    }

    // 툴바 설정 메서드
    fun settingToolbar(){
        fragmentShowBinding.apply {
            // 타이틀
            toolbarShow.title = "동물 정보 보기"
            // 네비게이션
            toolbarShow.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarShow.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
            }
            // 메뉴
            toolbarShow.inflateMenu(R.menu.show_toolbar_menu)
            toolbarShow.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.show_toolbar_menu_edit -> {
                        // 동물 번호를 담아준다.
                        val dataBundle = Bundle()
                        dataBundle.putInt("animalIdx", arguments?.getInt("animalIdx")!!)
                        // ModifyFragment로 이동한다.
                        mainActivity.replaceFragment(FragmentName.MODIFY_FRAGMENT, true, dataBundle)
                    }
                    R.id.show_toolbar_menu_delete -> {
                        // mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
                        // 삭제를 위한 다이얼로그를 띄운다.
                        deleteAnimalInfo()
                    }
                }
                true
            }
        }
    }

    // TextView에 값을 설정하는 메서드
    fun settingTextView(){
        // 만일의 경우를 위해 TextView들을 초기화 해준다.
        fragmentShowBinding.textViewShowType.text = ""
        fragmentShowBinding.textViewShowName.text = ""
        fragmentShowBinding.textViewShowAge.text = ""
        fragmentShowBinding.textViewShowGender.text = ""
        fragmentShowBinding.textViewShowFood.text = ""

        // 동물 번호를 추출한다.
        val animalIdx = arguments?.getInt("animalIdx")
        // 동물 데이터를 가져와 출력한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                AnimalRepository.selectAnimalByAnimalIdx(mainActivity, animalIdx!!)
            }
            val animalViewModel = work1.await()

            fragmentShowBinding.textViewShowType.text = animalViewModel.animalType.str
            fragmentShowBinding.textViewShowName.text = animalViewModel.animalName
            fragmentShowBinding.textViewShowAge.text = animalViewModel.animalAge.toString()
            fragmentShowBinding.textViewShowGender.text = animalViewModel.animalGender.str
            fragmentShowBinding.textViewShowFood.text = animalViewModel.animalFavoriteSnack

            // 동물 종류에 따라 이미지 변경
            when (animalViewModel.animalType.str) {
                "강아지" -> {fragmentShowBinding.ImageViewShowType.setImageResource(R.drawable.dog_35px)
                    fragmentShowBinding.ImageViewShowType.setColorFilter(Color.parseColor("#785D12"))}

                "앵무새" -> {fragmentShowBinding.ImageViewShowType.setImageResource(R.drawable.parrot_35px)
                    fragmentShowBinding.ImageViewShowType.setColorFilter(Color.parseColor("#FF5E00"))}

                // 기본 이미지 설정
                else -> {fragmentShowBinding.ImageViewShowType.setImageResource(R.drawable.cat_35px)
                    fragmentShowBinding.ImageViewShowType.setColorFilter(Color.parseColor("#F361A6"))}
            }
        }
    }

    // 삭제처리 메서드
    fun deleteAnimalInfo(){
        // 다이얼로그를 띄워주다.
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
        materialAlertDialogBuilder.setTitle("삭제")
        materialAlertDialogBuilder.setMessage("삭제를 할 경우 복원이 불가능합니다")
        materialAlertDialogBuilder.setNeutralButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    val animalIdx = arguments?.getInt("animalIdx")
                    AnimalRepository.deleteAnimalInfoByAnimalIdx(mainActivity, animalIdx!!)
                }
                work1.join()
                mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
            }
        }
        materialAlertDialogBuilder.show()
    }
}