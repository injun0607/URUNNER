<template>
    <div id="notice" align=center>
        <h3 style="padding:30px 0px 0px 0px; font-weight:bold">유러너 공지사항</h3>
    <div class="게시물목록">
        <notice-list :notices="notices"/>
    </div>
        <!--<router-link :to="{ name: 'ManagerNoticeRegister' }" style="text-decoration: none;">
            <v-btn text color="black" rounded x-large
                style="padding: 10px; width: 90px;">
            글쓰기
            </v-btn>
        </router-link>-->
    </div>
</template>

<script>

import NoticeList from '@/components/notice/NoticeList.vue'
import { mapState, mapActions } from 'vuex'

export default {
    name: 'NoticeListPage',
    components: {
        NoticeList
    },
    computed: {
        ...mapState(['notices'])
    },
    mounted () {
        this.fetchNoticeList()
    },
    methods: {
        ...mapActions(['fetchNoticeList'])
    }
}
</script>

<style scoped>

.게시물목록 {
  padding:0px 50px 0px 50px;
}
</style>