<template>
    <div class="main">
        <!-- 사이드바 -->
        <div class="sideBar hidden-sm-and-down">
            <v-card class="mx-auto0" max-width="500">
                <v-list>
                    <div class="title" @click="callAll()">전체 보기</div>
                    <v-list-group
                        v-for="item in items"
                        :key="item.title"
                        v-model="item.active"
                        :prepend-icon="item.action"
                        no-action="no-action">
                        <template v-slot:activator>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.title"></v-list-item-title>
                            </v-list-item-content>
                        </template>
                        <v-list-item v-for="child in item.items" :key="child.value">
                            <v-list-item-content>
                                <!-- <v-btn @click="selectCategory(child)" style="cursor:pointer">{{child.title}}</v-btn> -->
                                <v-list-item-title class="child" v-text="child.title" @click="selectCategory(child)" style="cursor:pointer"></v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>
                    </v-list-group>
                </v-list>
            </v-card>
            <div class="side_bar_option">
                <div class="forCheckBox">
                    <span style="padding-left:2px;">난이도 선택</span>
                    <div class="ratingLineAll" @click="difValue=null">전체 선택</div>
                    <div class="priceLine" :class="{ on : difValue=='중급 이상' }" @click="difValue='중급 이상'">중급 이상</div>
                    <div class="priceLine" :class="{ on : difValue=='초급' }" @click="difValue='초급'">초급</div>
                    <div class="priceLine" :class="{ on : difValue=='입문' }" @click="difValue='입문'">입문</div>
                </div>
                <div class="forCheckBox">
                    <span style="padding-left:2px;">별점 선택</span>
                        <div class="ratingLineAll" @click="ratingValue=null">전체 선택</div>
                        <div class="ratingLine" :class="{ on : ratingValue==4 }" @click="ratingValue=4">
                            <div><v-rating v-model="temp04" background-color="orange lighten-3" 
                        small dense color="orange" large readonly></v-rating></div><div class="ratingLineText">4점 이상</div></div>
                        <div class="ratingLine" :class="{ on : ratingValue==3 }" @click="ratingValue=3">
                            <div><v-rating v-model="temp03" background-color="orange lighten-3" 
                        small dense color="orange" large readonly></v-rating></div><div class="ratingLineText">3점 이상</div></div>
                        <div class="ratingLine" :class="{ on : ratingValue==2 }" @click="ratingValue=2">
                            <div><v-rating v-model="temp02" background-color="orange lighten-3" 
                        small dense color="orange" large readonly></v-rating></div><div class="ratingLineText">2점 이상</div></div>
                        <div class="ratingLine" :class="{ on : ratingValue==1 }" @click="ratingValue=1">
                            <div><v-rating v-model="temp01" background-color="orange lighten-3" 
                        small dense color="orange" large readonly></v-rating></div><div class="ratingLineText">1점 이상</div></div>
                </div>
                <div class="forCheckBox">
                    <span style="padding-left:2px;">가격</span>
                    <div class="ratingLineAll" @click="priceValue=null">전체 선택</div>
                    <div class="priceLine" :class="{ on : priceValue==50000 }" @click="priceValue=50000">5만원 이하</div>
                    <div class="priceLine" :class="{ on : priceValue==100000 }" @click="priceValue=100000">10만원 이하</div>
                    <div class="priceLine" :class="{ on : priceValue==200000 }" @click="priceValue=200000">20만원 이하</div>
                    <div class="priceLine" :class="{ on : priceValue==400000 }" @click="priceValue=400000">40만원 이하</div>
                </div>
            </div>
        </div>
        <!-- 본문 -->
        <div class="main_box">
            <!-- 공백(추후 배너창 이용) -->
            <div class="temp22 hidden-sm-and-down">
            </div>
            <!-- 검색창 + complete 분류 -->
            <v-spacer class="forLine0">

                <div class="forLine0sButton">
                </div>
                <div class="searching_box_top">
                    <div class="mr-9 hidden-sm-and-down">
                        <div class="searching" >
                            <span>
                                <input type="text" placeholder="검색어를 입력해주세요" v-model="word"
                                @keyup.enter="searchingWord(word)">
                            </span>
                               <v-icon class="searching_icon" @click="searching(word)">mdi-magnify</v-icon>
                        </div>
                    </div>
                </div>
            </v-spacer>
            <!-- 분류창 -->
            <v-spacer class="forLine">
                <v-icon>mdi-animation-outline</v-icon>
                <b class="tag_button hidden-sm-and-down" @click="callAll()">&nbsp;&nbsp;ALL</b>
                <b class="tag_button hidden-md-and-up" @click="callAll()">&nbsp;&nbsp;전체 보기</b>&nbsp;
                <b class="tag_button" v-show="path !== ''">＞{{ path }}</b>
                <b class="pathLine" :class="{ on : difValue !== null }">&nbsp;＞&nbsp;{{ difValue }}</b>
                <b class="pathLine2" :class="{ on : priceValue !== null }">&nbsp;＞&nbsp;{{ priceValue }}원 이하</b>
                <b class="pathLine3" :class="{ on : ratingValue !== null }">&nbsp;＞&nbsp;별 {{ ratingValue }}개 이상</b>
            </v-spacer>
            <!-- 리스트 -->
            <v-container class="lecture01 mr-9 hidden-sm-and-down">
                <div v-show="!searchinOn">
                    <v-container class="lecture_box">
                        <div v-for="(mob, index) in paginatedData2" :key="mob.id" class="item">
                            <div class="lecture_card">
                                <div class="card_img" @click="goPage(mob.id)">
                                    <v-img :src="`http://localhost:7777/lecture/image/${mob.thumbPath}/${mob.writer}`" height="200px" width="300px"></v-img>
                                </div>                                
                                <b @click="goPage(mob.id)">    
                                    <!-- title -->
                                    <div class="card_text01">
                                        {{ mob.title }}
                                    </div>
                                    <!-- nickname -->
                                    <div class="card_text02">
                                        {{ mob.writer }}
                                    </div>
                                    <div class="card_text_gropu01">
                                        <!-- rating -->
                                        <div class="card_text03">
                                            <v-rating
                                            v-model="reviewData[index].avg"
                                            background-color="orange lighten-3" small dense
                                            color="orange" large readonly></v-rating>
                                        </div>
                                        <!-- grade -->
                                        <div class="card_text04">
                                            {{ mob.grade }}
                                        </div>
                                    </div>
                                    <div class="forLine4"></div>
                                    <!-- price -->
                                    <div class="card_text05">
                                        {{ mob.price  | comma }}원
                                    </div>
                                </b>
                                <div style="width:1px;height:1px;">
                                    <div class="btn-plus2">
                                        <span draggable="false">
                                            <b @click="goPage(mob.id)"> 
                                                <div class="desc_text">{{ mob.desc }}</div>
                                            </b>
                                            <div class="align-right" >
                                                <v-icon :color="wish[index] ? 'red' : 'white'" class="d-block pa-1" @click.prevent="toggleHeartBtn(mob, index)">
                                                mdi-cards-heart
                                                </v-icon>
                                                <v-icon :color="cart[index] ? 'blue' : 'white'" class="d-block pa-1" @click.prevent="toggleCartBtn(mob, index)">
                                                mdi-cart
                                                </v-icon>
                                            </div>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <v-container style="margin-top:20px;">
                        <div>
                            <v-pagination class="btn_pagination" v-model="pageNumber" :length="totalPages"></v-pagination>
                        </div>
                        </v-container>
                    </v-container>
                </div>
            </v-container>
            <!-- 모바일 리스트 -->
            <v-container class="lecture01 mr-9 hidden-md-and-up">
                <div v-show="!searchinOn">
                    <v-container class="lecture_box">
                        <div v-for="mob in paginatedData2" :key="mob.boardNo">
                            <div class="mx-auto2" @click="goPage(mob.id)">
                                <div class="card_img" @click="goPage(mob.id)">
                                    <v-img :src="`http://localhost:7777/lecture/image/${mob.thumbPath}/${mob.writer}`" height="150px" width="150px"></v-img>
                                </div>
                                <div class="card_info">                                    
                                    <div style="height:66px;"><!-- title -->
                                        {{mob.title}}
                                    </div>
                                    <div class="forLine4"></div>
                                    <div class="card_text">
                                        <div class="nickname_txt">
                                            <!-- nickname -->
                                            {{ mob.writer }}
                                        </div>
                                        <div></div><div></div>
                                        <div v-show="path != ''" class="category_txt">
                                            {{ path }}
                                        </div>
                                        <div class="grade_txt">
                                            <!-- grade -->
                                            {{ mob.grade }}
                                        </div>
                                    </div>
                                    <div class="card_text2">
                                        <div>
                                            <!-- price -->
                                            ￦{{ mob.price  | comma }}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </v-container>
                    <div style="margin-top:20px;">
                    <div class="text-center">
                        <v-pagination class="btn_pagination" v-model="pageNum2" :length="pageCount2"></v-pagination>
                    </div>
                    </div>
                </div>
            </v-container>
            <!-- 모바일 사이즈 때 나타나는 글쓰기 버튼 -->
            <v-row justify="center">
                <v-dialog v-model="dialog" scrollable max-width="300px">
                <template v-slot:activator="{ on, attrs }">
                    <v-btn color="primary" fab dark v-bind="attrs" v-on="on" fixed right style="top:83vh;left:85vw;" class="hidden-md-and-up">
                        <v-icon dark>mdi-plus</v-icon>
                    </v-btn>
                </template>
                <v-card>
                    <div class="title2" @click="callAll()">전체 보기</div>
                    <v-list-group
                        v-for="item in items"
                        :key="item.title"
                        v-model="item.active"
                        :prepend-icon="item.action"
                        no-action="no-action">
                        <template v-slot:activator>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.title"></v-list-item-title>
                            </v-list-item-content>
                        </template>
                        <v-list-item v-for="child in item.items" :key="child.value">
                            <v-list-item-content>
                                <!-- <v-btn @click="selectCategory(child)" style="cursor:pointer">{{child.title}}</v-btn> -->
                                <v-list-item-title class="child" v-text="child.title" @click="selectCategory(child)" style="cursor:pointer"></v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>
                    </v-list-group>
                </v-card>
                </v-dialog>
            </v-row>
        </div>
    </div>
</template>
<script>

import { mapState, mapActions } from 'vuex'
import axios from 'axios';

export default {
    name: 'ForSaleLectureList',
    props: {
        callLecturelist: {
            type: Array
        },
        pageSize2: {
            type: Number,
            required: false,
            default: 6
        }
    },
    data: () => ({
        pageNum2: 1,                
        pageNumS: 1,
        searchingResult: [],
        searchinOn: false,
        word: '',
        rating: 5,
        path: '',
        difValue: null,
        priceValue: null,
        ratingValue: null,
        temp04: 4,
        temp03: 3,
        temp02: 2,
        temp01: 1,
        copiedList: [],
        refreshCheck: 1,
        cart: [],
        wish: [],
        dialog: false,
        reviewData: [],
        pageNumber: null,
        totalPages: null
    }),
    created () {
        setTimeout(() => {
            this.copiedList = this.callLecturelist
            if(this.$store.state.tempCate !== null) {
                this.fetchCallLectureListWithCategory(this.$store.state.tempCate)
                this.$store.state.tempCate = null
            }
            // 위시리스트 생성            
            for (var i = 0; i < this.callLecturelist.length; i++) {
                this.$set(this.wish, i, this.callLecturelist[i].wishList)
            }
            // 카트리스트 생성            
            for (var j = 0; j < this.callLecturelist.length; j++) {
                this.$set(this.cart, j, this.callLecturelist[j].cart)
            }
            this.reviewData = this.$store.state.reviewData

            this.pageNumber = this.$store.state.pageData.number + 1
            this.totalPages = this.$store.state.pageData.totalPages
            }, 300)
    },
    watch: {
        word(newVal) {
            if(newVal == '') {
                setTimeout(() => {
                    this.searchinOn = false
                    }, 200)
            }
        },
        difValue() {
            this.sideBarFilter()
        },
        priceValue() {
            this.sideBarFilter()
        },
        ratingValue() {
            this.sideBarFilter()
        },
        pageNumber(newVal) {
            this.$store.state.pageNumber = newVal - 1
            this.fetchCallLectureListWithCategory(this.$store.state.tempCate)
            this.ratingValue = null
            this.difValue = null
            this.priceValue = null
        }
    },
    methods: {
        searching () {
            var lists = this.callLecturelist

            this.searchingResult = []
            for(var i = 0; i < lists.length; i++){
                if(lists[i].name.includes(this.word)){
                    this.searchingResult.push(lists[i])
                }
            }
            this.searchinOn = true
            
            if (this.word == '') {
                this.searchinOn = false
            }
        },
        selectCategory(data) {
            this.fetchCallLectureListWithCategory(data.title)
            .then(() => {
                this.copiedList = this.callLecturelist
                })
            this.path = data.title
            this.dialog = false
            this.ratingValue = null
            this.difValue = null
            this.priceValue = null
        },
        callAll() {
            this.$emit("callAll", {})
            this.word = '', this.path = '',
            this.priceValue = null, this.difValue = null, this.ratingValue = null
            this.dialog = false
        },
        searchingWord(data) {
            this.fetchCallLectureListWithFilter(data)
            this.path = ''            
        },
        goPage(data) {
            this.$router.push( { name: 'LectureDetailPage', params: { lectureId: data.toString() } } )
        },
        toggleHeartBtn(data, index) {
            const temp = data
        axios.get(`http://localhost:7777/manageLecture/addToWish/${temp.id}`)
                .then(({ data }) => {
                this.$set(this.wish, index, data)
                })
        },
        toggleCartBtn(data, index) {            
            const temp = data
        axios.get(`http://localhost:7777/manageLecture/addToCart/${temp.id}`)
                .then(({ data }) => {
                this.$set(this.cart, index, data)
                })
        },
        sideBarFilter() {
            if ( this.pageNumber !== 1) {
                this.pageNumber = 1
            }
            '일단 각 변수값 체크하고 굴리자 null이면 ㄴ 값이 있으면 ㄱ'
            // 초기화
            var avgLists = this.reviewData
            var tempLists = this.copiedList
            var searchingResult = []
            var searchingResult2 = []            
            var searchingResult3 = []

            if(this.ratingValue !== null) {
                for(var k = 0; k < tempLists.length; k++){
                    if(avgLists[k].avg >= this.ratingValue) {
                        searchingResult.push(tempLists[k])
                    }
                }
            } else if (this.ratingValue == null) {
                searchingResult = tempLists
            }


            if(this.difValue !== null) {
                for(var i = 0; i < searchingResult.length; i++){                    
                    const regex = new RegExp(this.difValue, "gi");
                    const comparison = regex.test(searchingResult[i].grade)
                    if(comparison){
                        searchingResult2.push(searchingResult[i])
                    }
                }
            } else if (this.difValue == null) {
                searchingResult2 = searchingResult
            }



            if(this.priceValue !== null) {
                for(var j = 0; j < searchingResult2.length; j++){
                    if(searchingResult2[j].price < this.priceValue) {
                        searchingResult3.push(searchingResult2[j])
                    }
                }
            } else if (this.priceValue == null) {
                searchingResult3 = searchingResult2
            }

            
            this.callLecturelist = searchingResult3
            this.refreshCheck = 2
        },
        ...mapActions(['fetchCallLectureListWithCategory']),
        ...mapActions(['fetchCallLectureListWithFilter'])
        
    },
    computed: {
        pageCount2() {
            let listLength = this.callLecturelist.length, // 길이
                listSize = this.pageSize2,
                page = Math.floor(listLength / listSize);
            if (listLength % listSize > 0) 
                page += 1;
                return page;
        },
        paginatedData2() {
            return this.callLecturelist;
        },
        items() {
            return this.$store.state.category2
        },
        ...mapState ({
        lists: state => state.lists
        })
    },
    filters : {
        comma(val){
            return String(val).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
    }
}
    
</script>

<style scoped>
.main {
    display: flex;
    justify-content: start;
}
.sideBar {
    margin: 60px 60px 0px 10vw;
    cursor: pointer;
}
.mx-auto0.v-card.v-sheet.theme--light {
    width: 200px;
}


/* 사이드바 차일드 박스 */
.v-list-item.theme--light {
    padding-left:30px!important;
    border-top: 1px solid #EEEEEE;
    background-color: white;
    font-weight: 500;
}
.v-list-item.theme--light:hover {
    background-color: #FAFAFA;
}
.v-list-group.v-list-group--no-action {
    background-color: #f8f8f8 !important;
}


/* 사이드바 옵션 박스*/
.side_bar_option {
    font-size: 14px;
    font-weight: bold;
    border-top: 1px solid #BDBDBD;
}
.forCheckBox {
    display: flex;
    justify-content: start;
    flex-direction: column;
    border-bottom: 1px solid #BDBDBD;
    padding: 15px 5px;
}
.v-input.v-input--hide-details.theme--light.v-input--selection-controls.v-input--checkbox {
    margin: 0px;
}
.v-input.v-input--hide-details.theme--light.v-input--selection-controls.v-input--checkbox:hover {
    background-color: #f8f8f8 !important;
}
.ratingLineAll {
    display:flex;
    justify-content: start;
    font-size: 12px;
    font-weight: bold;
    padding-left: 3px;
    margin-top: 5px;
}
.ratingLine {
    display:flex;
    justify-content: start;
    font-size: 12px;
    font-weight: 500;    
}
.ratingLine:hover {
    background-color: #f8f8f8 !important;
}
.ratingLine.on {
    background-color: #f8f8f8 !important;
    font-weight: bold;
    font-size: 12px;
    color: #01579B;
    transition: all 1s ease;
}
.ratingLineText {
    padding-top: 2px;
    margin-left: 3px;
}
.priceLine:hover {
    background-color: #f8f8f8 !important;
}
.priceLine.on {
    background-color: #f8f8f8 !important;
    font-weight: bold;
    font-size: 13px;
    color: #01579B;
}
.priceLine {
    display:flex;
    justify-content: start;
    font-size: 12px;
    font-weight: 500;
    padding: 3px 1px;
    margin-left:2px;   
}




.mx-auto:hover {
    transform: translate3d(0px, -1px, 0px);
    box-shadow: 10px 17px 40px 0 rgb(0 0 0 / 15%);
    transition: all 1s ease;
}
.data_table_box {
    text-align: center;
    width: 70vw;
}
.lecture01 {
    display: inline-block;
    margin: 0;
    padding: 0;
    width: 70vw;    
    min-width: 440px;
    max-width: 1000px;
}
.lecture_box {
    margin: 15px;
    padding: 0;
    width: 100vw;
    max-width: 900px;
    display: flex;
    flex-wrap: wrap;
    justify-content: start;
}
.btn_pagination {
    background-color: transparent;
    box-shadow: none;
}
p {
    margin: 0;
}
.v-btn.v-btn--text.theme--light.v-size--default {
    padding: 0;
}
.v-card__actions {
    padding: 4px;
}
.option_box {
    display: flex;
    justify-content: flex-end;
    width: 70vw;
    max-width: 1300px;
}
.searching_box_top {
    height: 50px;
    max-width: 1110px;
    display: flex;
    justify-content: space-between;
}
.searching_box {    
    height: 50px;
}
.searching_bar {
    display: flex;
    justify-content: row;
    height: 40px;
    width:70vw;
    max-width: 1300px;
    border: 1px solid #BDBDBD;
}
.searching {
    display: flex;
    justify-content: space-between;
    height: 43px;
    width: 350px;
    min-width: 300px;
    padding-left: 10px;
    max-width: 955px;
    border: 1px solid #BDBDBD;
}
.title_box span {
    font-size: 25px;
    font-weight: bold;
}
.searching_message_box {
    width:70vw;
    height: 110px;
    max-width: 1300px;
    display:flex;
    justify-content: center;
}
.searching_message {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width:60vw;
    max-width: 900px;
    border-bottom: 1px solid #BDBDBD;
    margin-top: 15px;
}
.searching_message div {
    text-align: center;
    font-size: 20px;
    margin-bottom: 10px;
}
.searching_message b {
    letter-spacing: 3px;
    color: #0288D1;
    font-size: 30px;
}
.searching_message p {    
    font-size: 13px;
    color: #757575;
}
.page_title {
    color: #424242;
}
.title {
    font-size: 15px;
    color: #0288D1;
    font-weight: 700;
}
.title2 {
    display: flex;
    align-items: center;
    font-size: 15px;
    font-weight: 700;
    background-color: rgb(246, 246, 246);
    height: 45px;
    border-bottom: 1px solid #EEEEEE;
    padding-left: 15px;
    cursor: pointer;
}
.title2:hover {
    background-color: #eeeeee;
}


.v-list-item.theme--light {
    padding-left:30px!important;
    border-top: 1px solid #EEEEEE;
    background-color: white;
    font-weight: 500;
}
.v-list-group.v-list-group--no-action {
    background-color: #f8f8f8 !important;
}











.mx-auto {
  z-index: 0;
  width:inherit;
  height:inherit;
}
.mx-auto:hover .btn-plus {
  opacity:1;
  transform:scale(1);
}
/* .lecture_card:hover .btn-plus2 {
  opacity:1;
} */
.btn_pagination {
    background-color: transparent;
    box-shadow: none;
}
.nothing {
    width: 290px;
}
.nothing p {
    margin: 0px;
}
.item {
    margin: 10px;
    width: 270px;
    cursor: pointer;
}
.item_m {
    margin: 10px;
    width: 200px;
    cursor: pointer;
}
.tag_button {
    color: black;
    cursor: pointer;
}
.tag_button.on {
    color: #F9A825;
}
.tag_button.on2 {
    color: rgb(53, 53, 53);
    font-weight: bold;
}
.tag_button:hover {
    color: rgb(63, 63, 63);
    cursor: pointer;
    transition: all 0.f5s ease;
}
.forLine0 {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 40px !important;
    padding-left: 2vw;
}
.forLine0sButton {
    display: flex;
    justify-content: start;
    align-items: center;
}
.forLine {
    height: 40px !important;
    border-top: 1px solid #BDBDBD;
    border-bottom: 1px solid #BDBDBD;
    display: flex;
    justify-content: start;
    align-items: center;
    margin-top: 10px;
}
/* 분류창 */
.pathLine {
    transition: all 0.5s ease;
    opacity: 0;
    width:10px
}
.pathLine.on {
    transition: all 0.5s ease;
    opacity: 1;
    width: inherit;    
}
.pathLine2 {
    opacity: 0;
    width:10px
}
.pathLine2.on {
    transition: all 0.5s ease;
    opacity: 1;
    width: inherit;
}
.pathLine3 {
    transition: all 0.5s ease;
    opacity: 0;
}
.pathLine3.on {
    transition: all 0.5s ease;
    opacity: 1;
    width: inherit;
}


.searching {
    display: flex;
    justify-content: space-between;
    height: 43px;
    width: 350px;
    min-width: 300px;
    padding-left: 10px;
    max-width: 955px;
    border: 1px solid #BDBDBD;
}
.searching:hover {
    display: flex;
    justify-content: space-between;
    height: 43px; 
    width: 350px;
    padding-left: 10px;
    max-width: 955px;
    border: 1px solid rgb(155, 155, 155);
}
.searching span {
    display: flex;
    align-self: center;
}
.searching span input {
    margin-top: 3px;
    width: 280px;
}
.searching_icon {
    padding: 11px 10px 10px 10px;
    border-left: 1px solid #BDBDBD;
    background-color: #FAFAFA;
}
.searching_icon:hover {
    padding: 11px 10px 10px 10px;
    border-left: 1px solid #BDBDBD;
    background-color: #dfdfdf;
}
input:focus {
    outline:none;
}
.main_box {
    justify-content: center;
    flex-direction: column;
    
    color: #424242;
    max-width:900px;
}
.temp2 {
    font-size: 15px;
    padding: 3px;
}
.card_text {
    display: flex;
    justify-content: space-around;
    font-size:11px;
    font-weight: 00;
    color:#424242;
    margin:5px 5px 0px k5px;
    padding-bottom:5px;
    border-bottom:1px solid #ececec
}
.card_text2 {
    display: flex;
    justify-content: start;
    font-size: 18px;
    font-weight: 700;
    color:black;
    margin:5px 5px 0px 5px;
    padding-bottom:5px;
    padding-left: 5px;
    border-bottom:1px solid #ececec
}
.v-sheet.v-card:not(.v-sheet--outlined) {
    box-shadow: none !important;

}
.forLine4 {
    padding-bottom:5px;
    border-bottom:1px solid #ececec
}
.nickname_txt {
    margin-top: 5px;
    font-size: 13px;
    font-weight: bold;
    color: #757575;
}
.grade_txt {
    margin-top: 5px;
    background-color: #C2185B;
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 20px;
    padding: 0 4px 0 4px;
    font-size: 11px;
    font-weight: bold;
}
.category_txt {
    margin-top: 5px;
    background-color: #F9A825;
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 20px;
    padding: 0 4px 0 4px;
    font-size: 11px;
    font-weight: bold;
}
.title {
    padding: 10px 16px !important;
    font-size: 12px !important;
    cursor: pointer;
    border: 1px solid #EEEEEE;
}
.title:hover {
    background-color: rgb(246, 246, 246);
}
.v-application .title {
    font-size: 1rem !important;
}
.v-list-item__title {
    font-size: 0.9rem;
}
/* 사이드바 차일드 텍스트 */
.v-list-item__title.child {
    font-size: 0.8rem !important;
}
/* 사이드바 타이틀 */
.v-application .primary--text {
    color: black !important;
    font-weight: bold;
    caret-color: #BDBDBD !important;
}
.v-list-group.v-list-group--no-action {
    font-size: 5px !important;
    border-bottom: 1px solid #EEEEEE;
    border-left: 1px solid #EEEEEE;
    border-right: 1px solid #EEEEEE;
    background-color: white;
}
.v-list-group__header .v-list-item .v-list-item--link .theme--light {
    padding-right: 0px !important;
}
.mx-auto2 {
    display:flex;
    justify-content: start;
    flex-direction: row;

    width: 90vw;
    margin: 10px 0px;
}
.card_info {
    display:flex;
    justify-content: start;
    flex-direction: column;
    padding: 5px 5px;
    width: inherit;
    font-size: 15px;
    font-weight: bold;
    color: #424242;
}
.v-list-group__items {
    background-color: white;
}






.v-image.v-responsive.theme--light {
  border-radius: 3px;
}
.lecture_card:hover .cardhover {
    opacity:1;
    transform:scale(1);
    transition: all 0.5s ease;  
}
.lecture_card {
  /* z-index: 0;
  width:inherit;
  height:inherit; */
}
.lecture_card:hover {
    /* transform: translate3d(0px, 0px, 0px); */
}
.lecture_card:hover .btn-plus {
  opacity:1;
  transform:scale(1);
  transition: all 0.5s ease;
}
.btn-plus {
  position:absolute;
  top:70px;
  left:105px;
  background:rgb(65, 84, 192, 0.8);
  width:60px;
  height:60px;
  border-radius:50%;
  text-align:center;
  /* 추가된 부분 */
  opacity:0;
  transform:scale(2);
}
.btn-plus span {
  font-size:2.3em;
  color:#ffffff;
  user-select:none;
}
.btn-plus2 {
    display: flex;
    align-items: flex-end;  
    /* hover시 사진 어둡게 */
    position: relative;
    top:-329px;
    background:rgba(0, 0, 0, 0.815);
    width:270px;
    height:200px;
    padding: 5px;
    text-align:center;
    border-radius: 3px;
    opacity:0;
}
.btn-plus2 span {
    display: flex;
    flex-direction: column;
    justify-content: start;
    font-size: 2.3em;
    color: #ffffff;
    user-select: none;
    opacity:0.8;
}
.btn-plus2:hover {
    opacity:1;
}


.cardhover {
    position:absolute;
    top:0px;
    padding: 10px;
    background:rgba(0, 0, 0, 0.5);
    width:270px;
    height:200px;
    text-align:center;
    color: white;
    border-radius: 3px;
    cursor: pointer;
    opacity: 0;
}
.card_text01 {    
    display:flex;
    justify-content: start;
    margin: 5px 0px;
    font-size: 14px;
    padding-left: 3px;
    height: 44px;

    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    line-height: 22px;
    -webkit-line-clamp: 2; /* 표시하고자 하는 라인 수 */
    -webkit-box-orient: vertical;
}
.card_text02 {
    display:flex;
    justify-content: start;
    margin: 8px 0px 0px 0px;
    font-size: 13px;
    font-weight: bold;
    padding-left: 3px;
}
.card_text_gropu01 {
    display: flex;
    justify-content: space-between;
    flex-direction: row;
}
.card_text03 {
    display:flex;
    justify-content: start;
    margin: 0px 0px;
    font-size: 14px;
}
.card_text04 {
    margin-top: 5px;
    background-color: #C2185B;
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 20px;
    padding: 0 4px 0 4px;
    font-size: 11px;
    font-weight: bold;
}
.card_text05 {
    display:flex;
    justify-content: start;
    margin: 0px 0px;
    font-size: 14px;
    font-weight: 900;
    padding-left: 3px;
}
.temp22 {
    margin-top:90px;
}
.text-center {
    width: 95vw;
}
a { text-decoration:none !important }
a:hover { text-decoration:none !important }


.box {
  width: 300px;
  height: 200px;
  overflow: hidden;
}

.box>a:hover img {
  transform: scale(1.5);
  transition: transform 1s;
  filter: brightness(70%);
}
.desc_text {
    font-size:13px;
    text-align:center;

    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    line-height: 22px;
    -webkit-line-clamp: 5; /* 표시하고자 하는 라인 수 */
    -webkit-box-orient: vertical;
}
</style>
