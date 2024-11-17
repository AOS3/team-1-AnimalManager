// 프래그먼트를 나타내는 값
enum class FragmentName(var number:Int, var str:String){
    // 첫 화면
    MAIN_FRAGMENT(1, "MainFragment"),
    // 입력 화면
    INPUT_FRAGMENT(2, "InputFragment"),
    // 출력 화면
    SHOW_FRAGMENT(3, "ShowFragment"),
    // 수정 화면
    MODIFY_FRAGMENT(4, "ModifyFragment"),
}

// 동물 타입을 나타내는 값
enum class AnimalType(var number:Int, var str:String){
    // 강아지
    Animal_TYPE_DOG(1, "강아지"),
    // 고양이
    Animal_TYPE_CAT(2, "고양이"),
    // 앵무새
    Animal_TYPE_PARROT(3, "앵무새")
}

enum class AnimalGender(var number: Int, var str:String) {
    // 수컷
    ANIMAL_GENDER_MALE(1, "수컷"),
    // 암컷
    ANIMAL_GENDER_FEMALE(2, "암컷")

}

// 간식
enum class AnimalFood(var number:Int, var str:String){
    FOOD_APPLE(1, "사과"),
    FOOD_BANANA(2, "바나나"),
    FOOD_ORANGE(3, "오렌지")
}