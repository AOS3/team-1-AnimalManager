# ViewBinding 설정

[build.gradle.kts]

```kt
    viewBinding{
        enable = true
    }
```

[MainActivity.kt]

```kt
    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
```

---

# MainFragment 구성

### FragmentContainerView를 배치한다.

[ activity_main.xml ]

```text
ConstraintLayout
    - id : main

    FragmentContainerView
        - id : containerMain
        - layout_width : 0dp
        - layout_height : 0dp
        - layout_constraintBottom_toBottomOf : parent
        - layout_constraintEnd_toEndOf : parent
        - layout_constraintStart_toStartOf : parent
        - layout_constraintTop_toTopOf : parent
```

### util 패키지를 만들어준다.

```text
com.lion.a061ex_roomdatabase.util
```

### Values.kt 파일을 util 패키지에 만들어준다.

### Fragment의 이름을 정의할 enum 클래스를 만들어준다.


```kt
// 프래그먼트를 나타내는 값
enum class FragmentName(var number:Int, var str:String){

}
```

### 라이브러리를 추가한다.

[build.gradle.kts]

```kt
plugins {
        ...
    kotlin("kapt")
}
```

```kt
dependencies {
        ...
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
}
```

### Fragment 관리 코드를 넣어준다.

[MainActivity.kt]

```kt
    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: FragmentName, isAddToBackStack:Boolean, dataBundle: Bundle?){
        // 프래그먼트 객체
        val newFragment = when(fragmentName){

        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {

            newFragment.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
            newFragment.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
            newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)

            replace(R.id.containerMain, newFragment)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: FragmentName){
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
```

### com.lion.a061ex_roomdatabase.fragment 패키지를 생성한다.

### MainFragment 를 생성한다.

### MainFragment 값을 추가한다.

[Values.kt - FragmentName]

```kt
    // 첫 화면
    MAIN_FRAGMENT(1, "MainFragment"),
```

### MainFragment 객체를 생성한다.

[MainActivity.kt - replaceFragment()]
```kt
            // 첫 화면
            FragmentName.MAIN_FRAGMENT -> MainFragment()
```

### MainFragment가 보여지도록 설정한다.

[MainActivity.kt - onCreate()]
```kt
        // 첫 화면을 설정한다.
        replaceFragment(FragmentName.MAIN_FRAGMENT, false, null)
```

### RecyclerView의 항목으로 사용할 layout을 만들어준다.

[res/layout/row_main.xml]

```text
LinearLayout
    - orientation : vertical
    - padding : 10dp
    - background : ?attr/selectableItemBackground

    TextView
        - id : textViewRowMainStudentName
        - textAppearance : Large
```

### FAB 버튼에 사용할 이미지를 추가해준다.

- res/drawable/add_24px.xml

### fragment_main.xml을 구성해준다.

[res/layout/fragment_main.xml]

```text
ConstraintLayout
    - transitionGroup : true

    MaterialToolbar
        - layout_constraintEnd_toEndOf : parent
        - layout_constraintStart_toStartOf : parent
        - layout_constraintTop_toTopOf : parent
        - background : @android:color/transparent
        - id : toolbarMain

    RecyclerView
        - layout_width : 0dp
        - layout_height : 0dp
        - layout_constraintBottom_toBottomOf : parent
        - layout_constraintEnd_toEndOf : parent
        - layout_constraintStart_toStartOf : parent
        - id : recyclerViewMain

    FloatingActionButton
        - layout_constraintBottom_toBottomOf : parent
        - layout_constraintEnd_toEndOf : parent
        - layout_marginEnd : 10dp
        - layout_marginBottom : 10dp
        - id : fabMainAdd
        - src : @drawable/add_24px
```

### MainFragment의 기본 코드를 작성한다.

[MainFragment.kt]
```kt
    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentMainBinding = FragmentMainBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        return fragmentMainBinding.root
    }
```

### RecyclerView 구성을 위한 임시 데이터를 정의한다.

[MainFragment.kt]
```kt
    // RecyclerView 구성을 위한 임시데이터
    val studentList = Array(50){
        "홍길동 $it"
    }
```

### RecyclerView의 어뎁터를 작성한다.

[MainFramgent.kt]

```kt
    // RecyclerView의 어뎁터
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>(){
        // ViewHolder
        inner class ViewHolderMain(val rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater, parent, false)
            val viewHolderMain = ViewHolderMain(rowMainBinding)
            return viewHolderMain
        }

        override fun getItemCount(): Int {
            return studentList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.rowMainBinding.textViewRowMainStudentName.text = studentList[position]
        }
    }
```

### Toolbar를 구성하는 메서드를 만들어준다.

[MainFragment.kt]

```kt
    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentMainBinding.apply {
            // 타이틀
            toolbarMain.title = "학생 목록"
        }
    }
```

### 메서드를 호출한다.

[MainFragment.kt - onCreateView()]
```kt
        // Toolbar를 구성하는 메서드 호출
        settingToolbar()
```

### RecyclerView를 구성하는 메서드를 만들어준다.

[MainFragment.kt]
```kt
    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentMainBinding.apply {
            // 어뎁터
            recyclerViewMain.adapter = RecyclerViewMainAdapter()
            // LayoutManager
            recyclerViewMain.layoutManager = LinearLayoutManager(mainActivity)
            // 구분선
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewMain.addItemDecoration(deco)
        }
    }
```

### 메서드를 호출한다

[MainFragment.kt - onCreateView()]
```kt
        // RecyclerView를 구성하는 메서드 호출
        settingRecyclerView()
```
---

# InputFragment 구성

### InputFragment를 생성한다.

### InputFragment의 이름을 정의해준다.

[Values.kt - FragmentName]
```kt
    // 입력 화면
    INPUT_FRAGMENT(2, "InputFragment"),
```

### InputFragment의 객체를 생성해준다.

[MainActivity.kt - replaceFragment()]
```kt
            // 입력 화면
            FragmentName.INPUT_FRAGMENT -> InputFragment()
```

### FAB를 구성해주는 메서드를 구현한다.

[MainFragment.kt]
```kt
    // fab를 구성하는 메서드
    fun settingFAB(){
        fragmentMainBinding.apply {
            fabMainAdd.setOnClickListener {
                // InputFragment로 이동한다.
                mainActivity.replaceFragment(FragmentName.INPUT_FRAGMENT, true, null)
            }
        }
    }
```

### 메서드를 호출한다.

[MainFragment.kt - onCreateView()]

```kt
        // FAB를 구성하는 메서드 호출
        settingFAB()
```

### drawable 폴더에 두 개의 이미지를 넣어준다.
- id_card_24px.xml
- person_24px.xml

### 입력 화면을 구성해준다.

[fragment_input.xml]
```text

LinearLayout
    - orientation : vertical
    - transitionGroup : true

    MaterialToolbar
        - id : toolbarInput
        - backgroud : @android:color/transparent

    LinearLayout
        - orientation : vertical
        - padding : 10dp

        TextView
            - id : 삭제
            - textAppearance : Large
            - text : 운동부

        MaterialButtonToggleGroup
            - id : toggleGroupType
            - layout_height : wrap_content
            - layout_marginTop : 10dp
            - singleSelection : true
            - checkedButton : @id/buttonTypeBaseBall

                Button
                    - id : buttonTypeBaseBall
                    - style : OutlinedButton
                    - layout_weight : 1
                    - text : 야구부

                Button
                    - id : buttonTypeBasketBall
                    - style : OutlinedButton
                    - layout_weight : 1
                    - text : 농구부

                Button
                    - id : buttonTypeSoccer
                    - style : OutlinedButton
                    - layout_weight : 1
                    - text : 축구부

            TextInputLayout
                - id : textFieldInputName
                - layout_height : wrap_content
                - startIconDrawable : ic_card_24px
                - hint : 이름
                - endIconMode : clear_text
                - layout_marginTop : 10dp

                    TextInputEditText
                        - hint : 삭제
                        - singleLine : true

            TextInputLayout
                - id : textFieldInputAge
                - layout_height : wrap_content
                - startIconDrawable : person_24px
                - hint : 나이
                - endIconMode : clear_text
                - layout_marginTop : 10dp

                    TextInputEditText
                        - hint : 삭제
                        - singleLine : true
                        - inputType : number
```

### Fragment 기본 코드를 작성한다.

[InputFragment.kt]

```kt
    lateinit var fragmentInputBinding: FragmentInputBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentInputBinding = FragmentInputBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentInputBinding.root
    }
```

### 뒤로 가기 아이콘을 drawable 폴더에 넣어준다.
- arrow_back_24px.xml

### 툴바 구성 메서드를 구현해준다.

[InputFragment.kt]

```kt
    // Toolbar를 설정하는 메서드
    fun settingToolbar(){
        fragmentInputBinding.apply {
            // 타이틀
            toolbarInput.title = "학생 정보 입력"
            // 네비게이션 아이콘
            toolbarInput.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarInput.setNavigationOnClickListener {
                // 이전 화면으로 돌아간다.
                mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
            }
        }
    }
```

### 메서드를 호출한다.

[InputFragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbar()
```

### drawable 폴더에 이미지를 넣어준다.
- check_24px.xml


### toolbar에 사용할 메뉴 파일을 작성한다.

[res/menu/input_toolbar_menu.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/input_toolbar_menu_done"
        android:icon="@drawable/check_24px"
        android:title="완료"
        app:showAsAction="always" />
</menu>
```

### 메뉴를 구성하는 코드를 작성한다.

[InputFragment.kt - settingToolbar()]
```kt
            toolbarInput.inflateMenu(R.menu.input_toolbar_menu)
            toolbarInput.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.input_toolbar_menu_done -> {
                        mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
                    }
                }
                true
            }
```

---

# ShowFragment 구성

### ShowFragment를 만들어준다.

### Fragment의 이름을 정의해준다.

[Values.kt - FragmentName]
```kt
    // 출력 화면
    SHOW_FRAGMENT(3, "ShowFragment"),
```

### Fragment 객체를 생성한다.

[MainActivity.kt - replaceFragment()]
```kt
            // 출력 화면
            FragmentName.SHOW_FRAGMENT -> ShowFragment()
```

### MainFragment 의 RecyclerView의 Adapter 클래스를 수정한다.
- click 이벤트를 구현해준다.

[MainFragment.kt - RecyclerViewMainAdapter]

```kt
        // ViewHolder
        inner class ViewHolderMain(val rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root), OnClickListener{
            override fun onClick(v: View?) {
                // ShowFragment로 이동한다.
                mainActivity.replaceFragment(FragmentName.SHOW_FRAGMENT, true, null)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolderMain = ViewHolderMain(rowMainBinding)

            // 리스너를 설정해준다.
            rowMainBinding.root.setOnClickListener(viewHolderMain)

            return viewHolderMain
        }
```

### fragment_show.xml을 구성해준다.

[res/layout/fragment_show.xml]

```text
LinearLayout
    - orientation : vertical
    - transitionGroup : true

    MaterialToolbar
        - id : toolbarShow
        - backgroud : @android:color/transparent

        LinearLayout
            - orientation : vertical
            - padding : 10dp

            TextView
                - id : textViewShowType
                - textAppearance : Large

            TextView
                - id : textViewShowName
                - textAppearance : Large
                - layout_marginTop : 10dp

            TextView
                - id : textViewShowAge
                - textAppearance : Large
                - layout_marginTop : 10dp
```

### 아이콘 파일을 drawable 폴더에 넣어준다.
- delete_24px.xml
- edit_24px.xml

### 메뉴 파일을 작성해준다.
[res/menu/show_toolbar_menu.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/show_toolbar_menu_edit"
        android:icon="@drawable/edit_24px"
        android:title="수정"
        app:showAsAction="always" />
    <item
        android:id="@+id/show_toolbar_menu_delete"
        android:icon="@drawable/delete_24px"
        android:title="삭제"
        app:showAsAction="always" />
</menu>
```

### ShowFragment에 기본 코드를 작성한다.

[ShowFragment.kt]
```kt
    lateinit var fragmentShowBinding: FragmentShowBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentShowBinding = FragmentShowBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        return fragmentShowBinding.root
    }
```

### Toolbar를 구성하는 메서드를 작성한다.

[ShowFragment.kt]

```kt
    // 툴바 설정 메서드
    fun settingToolbar(){
        fragmentShowBinding.apply {
            // 타이틀
            toolbarShow.title = "학생 정보 보기"
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

                    }
                    R.id.show_toolbar_menu_delete -> {
                        mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
                    }
                }
                true
            }
        }
    }
```

### 메서드를 호출한다.

```kt
        // 툴바 설정 메서드 호출
        settingToolbar()
```

---

# 수정 화면 구성

### ModifyFragment 를 만들어준다

### Fragment의 이름을 정의해준다.

[Values.kt - FragmentName]
```kt
    // 수정 화면
    MODIFY_FRAGMENT(4, "ModifyFragment"),
```

### ModifyFragment 객체를 생성한다

[MainActivity.kt - replaceFragment()]
```kt
            // 수정 화면
            FragmentName.MODIFY_FRAGMENT -> ModifyFragment()
```

### ModifyFragment로 이동한다.

[ShowFragment.kt - settingToolbar()]
```kt
                        // ModifyFragment로 이동한다.
                        mainActivity.replaceFragment(FragmentName.MODIFY_FRAGMENT, true, null)
```

### fragment_modify.xml 을 구성한다.

```text

LinearLayout
    - orientation : vertical
    - transitionGroup : true

    MaterialToolbar
        - id : toolbarModify
        - backgroud : @android:color/transparent

        LinearLayout
            - orientation : vertical
            - padding : 10dp

            TextView
                - id : 삭제
                - textAppearance : Large
                - text : 운동부

            MaterialButtonToggleGroup
                - id : toggleGroupModifyType
                - layout_height : wrap_content
                - layout_marginTop : 10dp
                - singleSelection : true
                - checkedButton : @id/buttonTypeBaseBall

                    Button
                        - id : buttonModifyTypeBaseBall
                        - style : OutlinedButton
                        - layout_weight : 1
                        - text : 야구부

                    Button
                        - id : buttonModifyTypeBasketBall
                        - style : OutlinedButton
                        - layout_weight : 1
                        - text : 농구부

                    Button
                        - id : buttonModifyTypeSoccer
                        - style : OutlinedButton
                        - layout_weight : 1
                        - text : 축구부

                TextInputLayout
                    - id : textFieldModifyName
                    - layout_height : wrap_content
                    - startIconDrawable : ic_card_24px
                    - hint : 이름
                    - endIconMode : clear_text
                    - layout_marginTop : 10dp

                        TextInputEditText
                            - hint : 삭제
                            - singleLine : true

                TextInputLayout
                    - id : textFieldModifyAge
                    - layout_height : wrap_content
                    - startIconDrawable : person_24px
                    - hint : 나이
                    - endIconMode : clear_text
                    - layout_marginTop : 10dp

                        TextInputEditText
                            - hint : 삭제
                            - singleLine : true
                            - inputType : number

```

### ModifyFragment의 기본 코드를 작성한다.

[ModifyFragment.kt]
```kt
    lateinit var fragmentModifyBinding: FragmentModifyBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentModifyBinding = FragmentModifyBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        return fragmentModifyBinding.root
    }
```

### 툴바 설정 메서드를 구현한다.

[ModifyFragment.kt]

```kt
    // Toolbar 설정 메서드
    fun settingToolbar(){
        fragmentModifyBinding.apply {
            // 타이틀
            toolbarModify.title = "학생 정보 수정"
            // 네비게이션
            toolbarModify.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarModify.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
            }
            // 메뉴
            toolbarModify.inflateMenu(R.menu.modify_toolbar_menu)
            toolbarModify.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.modify_toolbar_menu_done ->{
                        mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
                    }
                }
                true
            }
        }
    }
```

### 메서드를 호출한다.

[ModifyFragment.kt - onCreateView()]
```kt
        // Toolbar 설정 메서드를 호출한다.
        settingToolbar()
```

### 초기 값을 설정하는 메서드를 구현해준다.

[ModifyFragment.kt]

```kt
    // 입력 요소들 초기 설정
    fun settingInput(){
        fragmentModifyBinding.apply {
            // 운동부
            toggleGroupModifyType.check(R.id.buttonModifyTypeBaseBall)
            // 이름
            textFieldModifyName.editText?.setText("홍길동")
            // 나이
            textFieldModifyAge.editText?.setText("30")
        }
    }
```

### 메서드를 호출해준다.

[ModifyFragment.kt - onCreateView()]

```kt
        // 입력 요소들 초기 설정
        settingInput()
```

---

# 내부 기능을 위한 준비

### 패키지 생성

- util : 도구적인 역할을 하는 패키지
- fragment : 프래그먼트를 모아 놓은 패키지
- viewmodel : 화면을 구성하기 위해서 사용하는 Model 클래스를 모아 놓은 패키지
- fragment와 viewmodel을 합쳐서 하나로 만들어 쓰기도 한다.
- vo : 데이터 베이스나 서버로 부터 받아온 데이터를 담는 Model 클래스를 모아 놓은 패키지
- repository : vo 에 담겨진 데이터를 viewmodel에 담아주는 역할을 한다. dao에 있는 메서드를 호출한다.
- dao : 데이터베이스나 서버와 데이터를 주고 받는 부분을 한다. 

### 테이블과 연동될 VO 클래스를 작성해준다.

[StudentVO.kt]

```kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StudentTable")
data class StudentVO(
    @PrimaryKey(autoGenerate = true)
    var studentIdx:Int = 0,
    var studentName:String = "",
    var studentType:Int = 0,
    var studentAge:Int = 0
)
```

### dao 인터페이스를 구현한다.

[StudentDAO.kt]
```kt
import androidx.room.Dao

@Dao
interface StudentDAO {
}
```

### 데이터 베이스 파일을 작성한다.

[StudentDatabase.kt]
```kt
@Database(entities = [StudentVO::class], version = 1, exportSchema = true)
abstract class StudentDatabase : RoomDatabase(){
    abstract fun studentDAO() : StudentDAO

    companion object{
        var studentDatabase:StudentDatabase? = null
        @Synchronized
        fun getInstance(context: Context) : StudentDatabase?{
            synchronized(StudentDatabase::class){
                studentDatabase = Room.databaseBuilder(
                    context.applicationContext, StudentDatabase::class.java,
                    "Student.db"
                ).build()
            }
            return studentDatabase
        }

        fun destroyInstance(){
            studentDatabase = null
        }
    }
}

```

--- 

# 학생 정보 저장 처리

### dao에 학생 정보를 저장하는 메서드를 작성해준다.

```kt
    // 학생 정보 저장
    @Insert
    fun insertStudentData(studentVO: StudentVO)
```

### 운동 타입을 나타내는 값을 정의한다.

[Values.kt]

```kt
// 운동부 타입을 나타내는 값
enum class StudentType(var number:Int, var str:String){
    // 야구부
    STUDENT_TYPE_BASEBALL(1, "야구부"),
    // 농구부
    STUDENT_TYPE_BASKETBALL(2, "농구부"),
    // 축구부
    STUDENT_TYPE_SOCCER(3, "축구부")
}
```

### 학생 정보를 담을 ViewModel 클래스를 작성해준다.

[StudentViewModel.kt]

```kt
data class StudentViewModel(
    var studentIdx:Int,
    var studentType:StudentType,
    var studentName:String,
    var studentAge:Int)
```

### 학생 정보를 관리할 Repository 클래스를 만들어준다.

[StudentRepository.kt]

```kt
class StudentRepository {

    companion object{
        
    }
}
```

### 학정 정보를 저장하는 메서드를 구현한다.

[StudentRepository.kt]
```kt
        // 학생 정보를 저장하는 메서드
        fun insertStudentInfo(context: Context, studentViewModel: StudentViewModel){
            // 데이터베이스 객체를 가져온다.
            val studentDatabase = StudentDatabase.getInstance(context)
            // ViewModel에 있는 데이터를 VO에 담아준다.
            val studentType = studentViewModel.studentType.number
            val studentName = studentViewModel.studentName
            val studentAge = studentViewModel.studentAge

            val studentVO = StudentVO(studentType = studentType, studentName = studentName, studentAge = studentAge)

            studentDatabase?.studentDAO()?.insertStudentData(studentVO)
        }
```

### 저장하는 메서드를 호출한다.

[InputFragment.kt - settingToolbar()]
```kt
                        // 사용자가 입력한 데이터를 가져온다.
                        // 운동부
                        val studentType = when(toggleGroupType.checkedButtonId){
                            // 야구부
                            R.id.buttonTypeBaseBall -> StudentType.STUDENT_TYPE_BASEBALL
                            // 농구부
                            R.id.buttonTypeBasketBall -> StudentType.STUDENT_TYPE_BASKETBALL
                            // 축구부
                            else -> StudentType.STUDENT_TYPE_SOCCER
                        }
                        // 이름
                        val studentName = textFieldInputName.editText?.text.toString()
                        // 나이
                        val studentAge = textFieldInputAge.editText?.text.toString().toInt()
                        // 객체에 담는다.
                        val studentViewModel = StudentViewModel(0, studentType, studentName, studentAge)

                        // 데이터를 저장하는 메서드를 코루틴으로 운영한다.
                        CoroutineScope(Dispatchers.Main).launch {
                            // 저장작업이 끝날때까지 대기한다.
                            async(Dispatchers.IO){
                                // 저장한다.
                                StudentRepository.insertStudentInfo(mainActivity, studentViewModel)
                            }
                            // 저장작업이 모두 끝나면 이전 화면으로 돌아간다.
                            mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
                        }
```

--- 

# 학생 목록 가져오기

### 학생 정보 전체를 가져오는 메서드를 구현한다.

[StudentDAO.kt]

```kt
    // 학생 정보를 가져오는 메서드
    @Query("""
        select * from StudentTable 
        order by studentIdx desc""")
    fun selectStudentDataAll() : List<StudentVO>
```

### 학생 정보 전체를 가져오는 메서드를 구현한다.
[StudentRepository.kt]

```kt
        // 학생 정보 전체를 가져오는 메서드
        fun selectStudentInfoAll(context: Context) : MutableList<StudentViewModel>{
            // 데이터 베이스 객체
            val studentDatabase = StudentDatabase.getInstance(context)
            // 학생 데이터 전체를 가져온다
            val studentVoList = studentDatabase?.studentDAO()?.selectStudentDataAll()
            // 학생 데이터를 담을 리스트
            val studentViewModelList = mutableListOf<StudentViewModel>()
            // 학생의 수 만큼 반복한다.
            studentVoList?.forEach {
                // 학생 데이터를 추출한다.
                val studentType = when(it.studentType){
                    StudentType.STUDENT_TYPE_BASEBALL.number -> StudentType.STUDENT_TYPE_BASEBALL
                    StudentType.STUDENT_TYPE_BASKETBALL.number -> StudentType.STUDENT_TYPE_BASKETBALL
                    else -> StudentType.STUDENT_TYPE_SOCCER
                }
                val studentName = it.studentName
                val studentAge = it.studentAge
                val studentIdx = it.studentIdx
                // 객체에 담는다.
                val studentViewModel = StudentViewModel(studentIdx, studentType, studentName, studentAge)
                // 리스트에 담는다.
                studentViewModelList.add(studentViewModel)
            }
            return studentViewModelList
        }
```

### MainFragment의 RecyclerView를 구성하기 위한 데이터를 변경한다.

[MainFragment.kt]

```kt
    // RecyclerView 구성을 위한 임시데이터
//    val studentList = Array(50){
//        "홍길동 $it"
//    }
    var studentList = mutableListOf<StudentViewModel>()
```

### Adapter의 코드를 변경해준다.

[MainFragment.kt - RecyclerViewMainAdapter]
```kt
        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.rowMainBinding.textViewRowMainStudentName.text = studentList[position].studentName
        }
```

### 데이터를 받아와 RecyclerView를 갱신하는 메서드를 만들어준다.

[MainFragment.kt]

```kt
    // 학생 정보를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshRecyclerViewMain(){
        // 학생 정보를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 학생정보를 가져온다.
                StudentRepository.selectStudentInfoAll(mainActivity)
            }
            studentList = work1.await()
            // RecyclerView를 갱신한다.
            fragmentMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
        }
    }
```

### 메서드를 호출해준다.

[MainFragment - onCreateView()]

```kt
        // RecyclerView 갱신 메서드를 호출한다.
        refreshRecyclerViewMain()
```

---

# 학생 정보 보기 구현

### RecyclerView의 항목을 눌렀을 때 ShowFragment로 이동될때 학생 번호를 전달한다.

[MainFragment.kt - RecyclerViewMainAdapter - ViewHolderMain - onClick()]
```kt
                // 사용자가 누른 학생의 학생 번호를 담아준다.
                val dataBundle = Bundle()
                dataBundle.putInt("studentIdx", studentList[adapterPosition].studentIdx)
                // ShowFragment로 이동한다.
                mainActivity.replaceFragment(FragmentName.SHOW_FRAGMENT, true, dataBundle)
```

### dao에 학생 한명의 정보를 가져오는 메서드를 만들어준다.

[StudentDAO]
```kt
    // 학생 한명의 정보를 가져오는 메서드
    @Query("""
        select * from StudentTable
        where studentIdx = :studentIdx
    """)
    fun selectStudentDataByStudentIdx(studentIdx:Int) : StudentVO
```

### repository에 학생 한명의 정보를 가져오는 메서드를 만들어준다.

[StudentRepository.kt]
```kt

        fun selectStudentInfoByStudentIdx(context: Context, studentIdx:Int) : StudentViewModel{
            val studentDatabase = StudentDatabase.getInstance(context)
            // 학생 한명의 정보를 가져온다.
            val studentVO = studentDatabase?.studentDAO()?.selectStudentDataByStudentIdx(studentIdx)
            // 학생 객체에 담는다
            val studentType = when(studentVO?.studentType){
                StudentType.STUDENT_TYPE_BASEBALL.number -> StudentType.STUDENT_TYPE_BASEBALL
                StudentType.STUDENT_TYPE_BASKETBALL.number -> StudentType.STUDENT_TYPE_BASKETBALL
                else -> StudentType.STUDENT_TYPE_SOCCER
            }
            val studentName = studentVO?.studentName
            val studentAge = studentVO?.studentAge

            val studentViewModel = StudentViewModel(studentIdx, studentType, studentName!!, studentAge!!)

            return studentViewModel
        }
```

### 데이터를 가져와 출력하는 메서드를 구현해준다.

[ShowFragment.kt]

```kt
    // TextView에 값을 설정하는 메서드
    fun settingTextView(){
        // 만일의 경우를 위해 TextView들을 초기화 해준다.
        fragmentShowBinding.textViewShowType.text = ""
        fragmentShowBinding.textViewShowName.text = ""
        fragmentShowBinding.textViewShowAge.text = ""
        // 학생 번호를 추출한다.
        val studentIdx = arguments?.getInt("studentIdx")
        // 학생 데이터를 가져와 출력한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                StudentRepository.selectStudentInfoByStudentIdx(mainActivity, studentIdx!!)
            }
            val studentViewModel = work1.await()

            fragmentShowBinding.textViewShowType.text = studentViewModel.studentType.str
            fragmentShowBinding.textViewShowName.text = studentViewModel.studentName
            fragmentShowBinding.textViewShowAge.text = studentViewModel.studentAge.toString()
        }
    }
```

### 메서드를 호출한다.

[ShowFragment.kt - onCreateView()]
```kt
        // 데이터를 가져와 출력한다.
        settingTextView()
```

---

# 정보 삭제 처리

### dao에 학생 정보를 삭제하는 메서들를 만들어준다.

[StudentDAO.kt]
```kt
    // 학생 한명의 정보를 삭제하는 메서드
    @Delete
    fun deleteStudentData(studentVO: StudentVO)
```

### 학생 정보를 삭제하는 메서드를 구현해준다.

[StudentRepository.kt]

```kt
        // 학생 정보 삭제
        fun deleteStudentInfoByStudentIdx(context: Context, studentIdx: Int){
            val studentDatabase = StudentDatabase.getInstance(context)
            // 삭제할 학생 번호를 담고 있을 객체를 생성한다.
            val studentVO = StudentVO(studentIdx = studentIdx)
            // 삭제한다
            studentDatabase?.studentDAO()?.deleteStudentData(studentVO)
        }
```

### 다이얼로그를 통해 삭제를 할 수 있도록 메서드를 구현해준다

[ShowFragment.kt]

```kt
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
                    val studentIdx = arguments?.getInt("studentIdx")
                    StudentRepository.deleteStudentInfoByStudentIdx(mainActivity, studentIdx!!)
                }
                work1.join()
                mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
            }
        }
        materialAlertDialogBuilder.show()
    }
```

### 메서드를 호출한다.

[ShowFragment.kt - settingToolbar()]
```kt
                        // mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
                        // 삭제를 위한 다이얼로그를 띄운다.
                        deleteStudentInfo()
```

---

# 정보 수정 처리

### 수정화면으로 이동할 때 학생 번호를 담아서 전달한다.

[ShowFragment.kt - settingToolbar()]

```kt
                        // 학생 번호를 담아준다.
                        val dataBundle = Bundle()
                        dataBundle.putInt("studentIdx", arguments?.getInt("studentIdx")!!)
                        // ModifyFragment로 이동한다.
                        mainActivity.replaceFragment(FragmentName.MODIFY_FRAGMENT, true, dataBundle)
```
### 입력 요소 초기화 부분을 수정해준다.

[ModifyFragment.kt]

```kt
    // 입력 요소들 초기 설정
    fun settingInput(){
        fragmentModifyBinding.apply {
            // 학생 번호를 가져온다.
            val studentIdx = arguments?.getInt("studentIdx")!!
            // 학생 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    StudentRepository.selectStudentInfoByStudentIdx(mainActivity, studentIdx)
                }
                val studentViewModel = work1.await()

                when(studentViewModel.studentType){
                    StudentType.STUDENT_TYPE_BASEBALL -> {
                        toggleGroupModifyType.check(R.id.buttonModifyTypeBaseBall)
                    }
                    StudentType.STUDENT_TYPE_BASKETBALL -> {
                        toggleGroupModifyType.check(R.id.buttonModifyTypeBasketBall)
                    }
                    StudentType.STUDENT_TYPE_SOCCER -> {
                        toggleGroupModifyType.check(R.id.buttonModifyTypeSoccer)
                    }
                }
                textFieldModifyName.editText?.setText(studentViewModel.studentName)
                textFieldModifyAge.editText?.setText(studentViewModel.studentAge.toString())
            }
        }
    }
```

### dao에 학생 정보를 수정하는 메서드를 정의해준다.

```kt
    // 학생 한명의 정보를 수정하는 메서드
    @Update
    fun updateStudentData(studentVO: StudentVO)
```

### repository에 학생 정보를 수정하는 메서드를 구현한다.

[StudentRepository.kt]
```kt
        // 학생 정보를 수정하는 메서드
        fun updateStudentInfo(context: Context, studentViewModel: StudentViewModel){
            val studentDatabase = StudentDatabase.getInstance(context)
            // VO에 객체에 담아준다
            val studentIdx = studentViewModel.studentIdx
            val studentType = studentViewModel.studentType.number
            val studentName = studentViewModel.studentName
            val studentAge = studentViewModel.studentAge
            val studentVO = StudentVO(studentIdx, studentName, studentType, studentAge)
            // 수정한다.
            studentDatabase?.studentDAO()?.updateStudentData(studentVO)
        }
```

### 수정 처리하는 메서드를 구현한다.

[ModifyFragment.kt]
```kt
    // 수정 처리 메서드
    fun modifyDone(){
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
        materialAlertDialogBuilder.setTitle("수정")
        materialAlertDialogBuilder.setMessage("이전 데이터로 복원할 수 없습니다")
        materialAlertDialogBuilder.setNeutralButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->
            // 수정할 데이터
            val studentIdx = arguments?.getInt("studentIdx")!!
            val studentType = when(fragmentModifyBinding.toggleGroupModifyType.checkedButtonId){
                R.id.buttonModifyTypeBaseBall -> StudentType.STUDENT_TYPE_BASEBALL
                R.id.buttonModifyTypeBasketBall -> StudentType.STUDENT_TYPE_BASKETBALL
                else -> StudentType.STUDENT_TYPE_BASKETBALL
            }
            val studentName = fragmentModifyBinding.textFieldModifyName.editText?.text.toString()
            val studentAge = fragmentModifyBinding.textFieldModifyAge.editText?.text.toString().toInt()

            val studentViewModel = StudentViewModel(studentIdx, studentType, studentName, studentAge)

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    StudentRepository.updateStudentInfo(mainActivity, studentViewModel)
                }
                work1.join()
                mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
            }
        }
        materialAlertDialogBuilder.show()
    }
```

### 메서드를 호출한다.

[ModifyFragment.kt - settingToolbar()]
```kt
                        // mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
                        modifyDone()
```