<template>
    <div style="display:flex;justify-content:center;margin:0px;">     
        <v-container v-show="role == 'ROLE_USER'" style="padding:0px;">
            <inq-board-for-user-list :boards="boards"/>
        </v-container>
        <v-container v-show="role == 'ROLE_USER,ROLE_MANAGER'" style="padding:0px;">
            <inq-board-list :boards="boards"/>
        </v-container>
    </div>
</template>

<script>

import InqBoardForUserList from '@/components/board/inq/InqBoardForUserList.vue'
import InqBoardList from '@/components/board/inq/InqBoardList.vue'
import { mapState, mapActions } from 'vuex'
import Vue from 'vue'

export default {
    name: 'InqBoardListForUserPage',    
    data() {
        return {
            role: Vue.$cookies.get("ROLES")
        }
    },
    components: {
        InqBoardForUserList,
        InqBoardList
    },
    computed: {
        ...mapState(['boards'])
    },
    mounted () {
        if (this.role == 'ROLE_USER,ROLE_MANAGER') {
            this.fetchInqBoardList()
        } else {
            this.fetchInqBoardForUserList(Vue.$cookies.get("USER_NAME"))
        }
    },
    methods: {
        ...mapActions(['fetchInqBoardList']),
        ...mapActions(['fetchInqBoardForUserList'])
        
    }
}
</script>