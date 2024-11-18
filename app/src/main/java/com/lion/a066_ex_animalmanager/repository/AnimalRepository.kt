package com.lion.a066ex_animalmanager.repository

import AnimalGender
import AnimalType
import android.content.Context
import com.lion.a066ex_animalmanager.viewmodel.AnimalViewModel
import com.lion.team1_project.dao.AnimalDatabase
import com.lion.team1_project.vo.AnimalVO

class AnimalRepository {

    companion object {

        // 동물 정보를 저장하는 메서드
        fun insertAnimalInfo(context: Context, animalViewModel: AnimalViewModel) {
            // 데이터베이스 객체를 가져온다.
            val animalDatabase = AnimalDatabase.getInstance(context)
            // ViewModel에 있는 데이터를 VO에 담아준다.
            val animalType = animalViewModel.animalType.number
            val animalName = animalViewModel.animalName
            val animalAge = animalViewModel.animalAge
            val animalGender = animalViewModel.animalGender.number
            val animalFavoriteSnack = animalViewModel.animalFavoriteSnack

            val animalVO = AnimalVO(
                animalType = animalType,
                animalName = animalName,
                animalAge = animalAge,
                animalGender = animalGender,
                animalFavoriteSnack = animalFavoriteSnack
            )

            animalDatabase?.animalDAO()?.insertAnimalData(animalVO)
        }

        // 동물 정보 전체를 가져오는 메서드
        fun selectAnimalInfoAll(context: Context): MutableList<AnimalViewModel> {
            // 데이터 베이스 객체
            val animalDatabase = AnimalDatabase.getInstance(context)
            // 동물 데이터 전체를 가져온다
            val animalVoList = animalDatabase?.animalDAO()?.selectAnimalDataAll()
            // 동물 데이터를 담을 리스트
            val animalViewModelList = mutableListOf<AnimalViewModel>()
            // 동물의 수 만큼 반복한다.
            animalVoList?.forEach {
                // 동물 데이터를 추출한다.
                val animalType = when (it.animalType) {
                    AnimalType.Animal_TYPE_DOG.number -> AnimalType.Animal_TYPE_DOG
                    AnimalType.Animal_TYPE_CAT.number -> AnimalType.Animal_TYPE_CAT
                    else -> AnimalType.Animal_TYPE_PARROT
                }
                val animalName = it.animalName
                val animalAge = it.animalAge
                val animalIdx = it.animalIdx
                val animalGender = when (it.animalGender) {
                    AnimalGender.ANIMAL_GENDER_MALE.number -> AnimalGender.ANIMAL_GENDER_MALE
                    else -> AnimalGender.ANIMAL_GENDER_FEMALE
                }
                val animalFavoriteSnack = it.animalFavoriteSnack

                // 객체에 담는다.
                val animalViewModel = AnimalViewModel(
                    animalIdx,
                    animalType,
                    animalName,
                    animalAge,
                    animalGender,
                    animalFavoriteSnack
                )
                // 리스트에 담는다.
                animalViewModelList.add(animalViewModel)
            }
            return animalViewModelList
        }

        // 동물 한 마리의 정보를 가져온다.
        fun selectAnimalByAnimalIdx(context: Context, animalIdx: Int): AnimalViewModel {
            val animalDatabase = AnimalDatabase.getInstance(context)
            // 동물 한 마리의 정보를 가져온다.
            val animalVo = animalDatabase?.animalDAO()?.selectAnimalDataByAnimalIdx(animalIdx)
            // 동물 객체에 담는다.
            val animalType = when (animalVo?.animalType) {
                AnimalType.Animal_TYPE_DOG.number -> AnimalType.Animal_TYPE_DOG
                AnimalType.Animal_TYPE_CAT.number -> AnimalType.Animal_TYPE_CAT
                else -> AnimalType.Animal_TYPE_PARROT
            }
            val animalName = animalVo?.animalName
            val animalAge = animalVo?.animalAge

            val animalGender = when (animalVo?.animalGender) {
                AnimalGender.ANIMAL_GENDER_MALE.number -> AnimalGender.ANIMAL_GENDER_MALE
                else -> AnimalGender.ANIMAL_GENDER_FEMALE
            }

            val animalFavoriteSnack = animalVo?.animalFavoriteSnack

            val animalViewModel = AnimalViewModel(
                animalIdx,
                animalType,
                animalName!!,
                animalAge!!,
                animalGender,
                animalFavoriteSnack!!
            )

            return animalViewModel
        }

        // 동물 정보를 수정하는 메서드
        fun updateAnimalInfo(context: Context, animalViewModel: AnimalViewModel) {
            val animalDatabase = AnimalDatabase.getInstance(context)
            // VO에 객체에 담아준다
            val animalIdx = animalViewModel.animalIdx
            val animalType = animalViewModel.animalType.number
            val animalName = animalViewModel.animalName
            val animalAge = animalViewModel.animalAge
            val animalGender = animalViewModel.animalGender.number
            val animalFavoriteSnack = animalViewModel.animalFavoriteSnack
            val animalVO = AnimalVO(animalIdx, animalName, animalAge, animalType, animalGender, animalFavoriteSnack)
            // 수정한다.
            animalDatabase?.animalDAO()?.updateAnimalData(animalVO)
        }

        // 동물 삭제 메서드
        fun deleteAnimalInfo(context: Context, animalViewModel: AnimalViewModel) {
            val animalDatabase = AnimalDatabase.getInstance(context)
            // VO에 객체에 담아준다.
            val animalIdx = animalViewModel.animalIdx
            val animalType = animalViewModel.animalType.number
            val animalName = animalViewModel.animalName
            val animalAge = animalViewModel.animalAge
            val animalGender = animalViewModel.animalGender.number
            val animalFavoriteSnack = animalViewModel.animalFavoriteSnack

            val animalVO = AnimalVO(animalIdx, animalName, animalAge, animalType, animalGender, animalFavoriteSnack)
            // 삭제한다.
            animalDatabase?.animalDAO()?.deleteAnimalData(animalVO)

        }


        // 동물 한마리만 정보 삭제
        fun deleteAnimalInfoByAnimalIdx(context: Context, animalIdx: Int){
            val animalDatabase = AnimalDatabase.getInstance(context)
            // 삭제할 동물 번호를 담고 있을 객체를 생성한다.
            val animalVO = AnimalVO(animalIdx = animalIdx)
            // 삭제한다
            animalDatabase?.animalDAO()?.deleteAnimalData(animalVO)
        }

    }
}