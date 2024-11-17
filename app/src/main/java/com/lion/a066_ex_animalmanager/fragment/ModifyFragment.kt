package com.lion.a066_ex_animalmanager.fragment

import AnimalFood
import AnimalGender
import AnimalType
import FragmentName
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a061ex_roomdatabase.repository.AnimalRepository
import com.lion.a061ex_roomdatabase.viewmodel.AnimalViewModel
import com.lion.a066_ex_animalmanager.MainActivity
import com.lion.a066_ex_animalmanager.R
import com.lion.a066_ex_animalmanager.databinding.FragmentModifyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// 동물 정보 수정 화면
class ModifyFragment : Fragment() {

    lateinit var fragmentModifyBinding: FragmentModifyBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as MainActivity

        fragmentModifyBinding = FragmentModifyBinding.inflate(layoutInflater)

        // 툴바 설정 메서드 호출
        settingToolbar()
        // 입력 요소들 초기 설정
        settingInput()

        return fragmentModifyBinding.root
    }

    // Toolbar 설정 메서드
    fun settingToolbar() {
        fragmentModifyBinding.apply {
            toolbarModify.title = "동물 정보 수정"
            // 뒤로 가기 (네비게이션)
            toolbarModify.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarModify.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
            }
            // 메뉴
            toolbarModify.inflateMenu(R.menu.toolbar_menu_modify_fragment)
            toolbarModify.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.modify_toolbar_menu_done -> {
                        modifyDone()
                    }
                }
                true
            }
        }
    }

    // 입력 요소들 초기 설정
    fun settingInput() {
        fragmentModifyBinding.apply {
            // 동물 번호를 가져온다.
            val animalIdx = arguments?.getInt("animalIdx")!!
            // 동물 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    AnimalRepository.selectAnimalByAnimalIdx(mainActivity, animalIdx)
                }
                val animalViewModel = work1.await()

                when(animalViewModel.animalType) {
                    AnimalType.Animal_TYPE_DOG -> {
                        toggleGroupTypeModifyFragment.check(R.id.buttonTypeDogModifyFragment)
                    }
                    AnimalType.Animal_TYPE_CAT -> {
                        toggleGroupTypeModifyFragment.check(R.id.buttonTypeCatModifyFragment)
                    }
                    AnimalType.Animal_TYPE_PARROT -> {
                        toggleGroupTypeModifyFragment.check(R.id.buttonTypeParrotModifyFragment)
                    }
                }
                textFieldNameModifyFragment.editText?.setText(animalViewModel.animalName)
                textFieldAgeModifyFragment.editText?.setText("${animalViewModel.animalAge}")

                when(animalViewModel.animalGender) {
                    AnimalGender.ANIMAL_GENDER_MALE -> {
                        radioGroupGenderModifyFragment.check(R.id.radioButtonMaleModifyFragment)
                    }
                    AnimalGender.ANIMAL_GENDER_FEMALE -> {
                        radioGroupGenderModifyFragment.check(R.id.radioButtonFemaleModifyFragment)
                    }
                }

                // 간식목록
                var snackList = animalViewModel.animalFavoriteSnack.split(" ")
                Log.d("test100", "snackList : ${snackList}")
                snackList.forEach {
                    when (it) {
                        AnimalFood.FOOD_APPLE.str ->{
                            chipGroupSnacksModifyFragment.check(R.id.chipAppleModifyFragment)
                        }
                        AnimalFood.FOOD_BANANA.str ->{
                            chipGroupSnacksModifyFragment.check(R.id.chipBananaModifyFragment)
                        }
                        else->{
                            chipGroupSnacksModifyFragment.check(R.id.chipOrangeModifyFragment)
                        }
                    }
                }
            }
        }
    }

    // 수정 처리 메서드
    fun modifyDone() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
        materialAlertDialogBuilder.setTitle("수정")
        materialAlertDialogBuilder.setMessage("이전 데이터로 복원할 수 없습니다.")
        materialAlertDialogBuilder.setNeutralButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("수정") {dialogInterface:DialogInterface, i:Int ->
            // 수정할 데이터
            val animalIdx = arguments?.getInt("animalIdx")!!
            val animalType = when(fragmentModifyBinding.toggleGroupTypeModifyFragment.checkedButtonId){
                R.id.buttonTypeDogModifyFragment -> AnimalType.Animal_TYPE_DOG
                R.id.buttonTypeCatModifyFragment -> AnimalType.Animal_TYPE_CAT
                else -> AnimalType.Animal_TYPE_PARROT
            }
            val animalName = fragmentModifyBinding.textFieldNameModifyFragment.editText?.text.toString()
            val animalAge = fragmentModifyBinding.textFieldAgeModifyFragment.editText?.text.toString().toInt()
            val animalGender = when(fragmentModifyBinding.radioGroupGenderModifyFragment.checkedRadioButtonId) {
                R.id.radioButtonMaleModifyFragment -> AnimalGender.ANIMAL_GENDER_MALE
                else -> AnimalGender.ANIMAL_GENDER_FEMALE
            }
            var animalFavoriteSnack = ""
            fragmentModifyBinding.chipGroupSnacksModifyFragment.checkedChipIds.forEach {
                when (it) {
                    R.id.chipAppleModifyFragment -> animalFavoriteSnack += " "+AnimalFood.FOOD_APPLE.str
                    R.id.chipBananaModifyFragment -> animalFavoriteSnack += " "+AnimalFood.FOOD_BANANA.str
                    R.id.chipOrangeModifyFragment -> animalFavoriteSnack += " "+AnimalFood.FOOD_ORANGE.str
                }
            }

            val animalViewModel = AnimalViewModel(animalIdx, animalType, animalName, animalAge, animalGender, animalFavoriteSnack.trim())

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    AnimalRepository.updateAnimalInfo(mainActivity, animalViewModel)
                }
                work1.join()
                mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
            }
        }
        materialAlertDialogBuilder.show()
    }
}