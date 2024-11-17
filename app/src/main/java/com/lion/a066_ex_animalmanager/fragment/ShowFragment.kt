package com.lion.a066_ex_animalmanager.fragment

import AnimalFood
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a061ex_roomdatabase.repository.AnimalRepository
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
                        // ModifyFragment로 이동한다.
                        val dataBundle = Bundle()
                        dataBundle.putInt("animalIdx",arguments?.getInt("animalIdx")!!)
                        mainActivity.replaceFragment(FragmentName.MODIFY_FRAGMENT, true, dataBundle)
                    }
                    R.id.show_toolbar_menu_delete -> {
                        deleteStudentInfo()
                    }
                }
                true
            }
        }
    }


    // TextView에 값을 설정하는 메서드
    fun settingTextView(){
        fragmentShowBinding.apply {
            // 동물 번호를 추출한다.
            val animalIdx = arguments?.getInt("animalIdx")
            // 동물 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    AnimalRepository.selectAnimalByAnimalIdx(mainActivity, animalIdx!!)
                }
                val animalViewModel = work1.await()

                Log.d("test 200", "viewModel : ${animalViewModel}")

                when(animalViewModel.animalType) {
                    AnimalType.Animal_TYPE_DOG -> {
                        toggleGroupTypeShowFragment.check(R.id.buttonTypeDogShowFragment)
                    }
                    AnimalType.Animal_TYPE_CAT -> {
                        toggleGroupTypeShowFragment.check(R.id.buttonTypeCatShowFragment)
                    }
                    AnimalType.Animal_TYPE_PARROT -> {
                        toggleGroupTypeShowFragment.check(R.id.buttonTypeParrotShowFragment)
                    }
                }
                textFieldNameShowFragment.text = "이름 : ${animalViewModel.animalName}"
                textFieldAgeShowFragment.text = "나이 : ${animalViewModel.animalAge}"


                when(animalViewModel.animalGender) {
                    AnimalGender.ANIMAL_GENDER_MALE -> {
                        radioGroupGenderShowFragment.check(R.id.radioButtonMaleShowFragment)
                    }
                    AnimalGender.ANIMAL_GENDER_FEMALE -> {
                        radioGroupGenderShowFragment.check(R.id.radioButtonFemaleShowFragment)
                    }
                }


                // 간식목록
                var snackList = animalViewModel.animalFavoriteSnack.split(" ")
                Log.d("test100", "snackList : ${snackList}")
                snackList.forEach {
                    when (it) {
                        AnimalFood.FOOD_APPLE.str ->{
                            chipGroupSnacksShowFragment.check(R.id.chipAppleShowFragment)
                        }
                        AnimalFood.FOOD_BANANA.str ->{
                            chipGroupSnacksShowFragment.check(R.id.chipBananaShowFragment)
                        }
                        else->{
                            chipGroupSnacksShowFragment.check(R.id.chipOrangeShowFragment)
                        }
                    }
                }
            }
        }



    }

    // 삭제처리 메서드
    fun deleteStudentInfo(){
        // 다이얼로그를 띄워주다.
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
        materialAlertDialogBuilder.setTitle("삭제")
        materialAlertDialogBuilder.setMessage("삭제를 할 경우 복원이 불가능합니다")
        materialAlertDialogBuilder.setNeutralButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    val animalIdx = arguments?.getInt("animalIdx")
                    val animalVo = AnimalRepository.selectAnimalByAnimalIdx(mainActivity, animalIdx!!)
                    AnimalRepository.deleteAnimalInfo(mainActivity, animalVo)
                }
                work1.join()
            }
            mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
        }
        materialAlertDialogBuilder.show()
    }

}