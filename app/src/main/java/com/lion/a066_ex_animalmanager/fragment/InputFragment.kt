package com.lion.a066_ex_animalmanager.fragment

import AnimalGender
import AnimalType
import FragmentName
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lion.a066ex_animalmanager.repository.AnimalRepository
import com.lion.a066ex_animalmanager.viewmodel.AnimalViewModel
import com.lion.a066_ex_animalmanager.MainActivity
import com.lion.a066_ex_animalmanager.R
import com.lion.a066_ex_animalmanager.databinding.FragmentInputBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InputFragment : Fragment() {

    lateinit var fragmentInputBinding: FragmentInputBinding
    lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentInputBinding = FragmentInputBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        settingToolbar()
        return fragmentInputBinding.root
    }

    //툴바
    fun settingToolbar() {
        fragmentInputBinding.apply {
            toolbarInputFragment.title = "동물 정보 입력"

            //네비게이션 아이콘
            toolbarInputFragment.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
            }

            //메뉴
            toolbarInputFragment.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.toolbar_menu_done_input_fragment -> {
                        // 사용자가 입력한 데이터 가져오기
                        // 동물 종
                        val animalType = when (toggleGroupTypeInputFragment.checkedButtonId) {
                            R.id.buttonTypeDogInputFragment -> AnimalType.Animal_TYPE_DOG
                            R.id.buttonTypeCatInputFragment -> AnimalType.Animal_TYPE_CAT
                            else-> AnimalType.Animal_TYPE_PARROT
                        }

                        // 이름
                        val animalName = textFieldInputNameInputFragment.editText?.text.toString()
                        // 나이
                        val animalAge = textFieldInputAgeInputFragment.editText?.text.toString().toInt()
                        // 성별
                        val animalGender = when (radioGroupGenderInputFragment.checkedRadioButtonId) {
                            R.id.radioButtonGenderMaleInputFragment -> AnimalGender.ANIMAL_GENDER_MALE
                            else -> AnimalGender.ANIMAL_GENDER_FEMALE
                        }
                        // 간식목록
                        var snackList = ""
                        chipGroupSnacksInputFragment.checkedChipIds.forEach {
                            when (it) {
                                R.id.chipAppleInputFragment -> {
                                    snackList+=" ${AnimalFood.FOOD_APPLE.str}"
                                }
                                R.id.chipBananaInputFragment ->{
                                    snackList+= " ${AnimalFood.FOOD_BANANA.str}"
                                }
                                else->{
                                    snackList+= " ${AnimalFood.FOOD_ORANGE.str}"
                                }
                            }
                        }
                        // 모델 생성
                        val animalViewModel = AnimalViewModel(0,animalType, animalName, animalAge, animalGender, snackList.trim())

                        // 데이터 저장
                        CoroutineScope(Dispatchers.Main).launch {
                            // 저장 작업 끝날때까지 대기
                            val work1 = async(Dispatchers.IO){
                                // 저장
                                AnimalRepository.insertAnimalInfo(mainActivity, animalViewModel)
                                Log.d("test100", "$animalViewModel")

                            }
                            // 리턴없이 삽입이라 조인
                            work1.join()
                            mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
                        }
                    }
                }
                true // 인덱스만 잘못된것 같아요~
            }

        }
    }



}