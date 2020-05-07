import { authStore } from "@/service/authStore";
import axios from "axios";
import Vue from "vue";
import Vuex from "vuex";
import VueAxios from "vue-axios";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    userToken: JSON.parse(localStorage.getItem("user_token") || "{}") || {},
    places: [],
    postCurrent: {},
    commentCurrent: {},
    posts: [],
  },
  mutations: {
    places(state, places) {
      state.places = places;
    },
    posts(state, posts) {
      state.posts = posts;
    },
    review(state, value) {
      state.postCurrent = value;
    },
    comment(state, value) {
      state.postCurrent = value;
    },
  },
  actions: {
    async getAllPlaces({ commit }) {
      await axios.get("http://localhost:8022/api/place").then((res) => {
        commit("places", res.data);
      });
    },

    review: async ({ commit }, { post }) => {
      console.log(post, 999999);
      await axios
        .post(`http://localhost:8022/api/post/create`, {
          content: post.content,
          idUser: post.idUser,
          idPlace: post.idPlace,
        })
        .then((result) => {
          commit("review", result.data);
        })
        .catch((err) => {
          alert(err);
        });
    },
    comment: async ({ commit }, { comment }) => {
      await axios
        .post(`http://localhost:8022/api/comment/create`, {
          content: comment.content,
          idUser: comment.idUser,
          idPost: comment.idPost,
        })
        .then((result) => {
          commit("comment", result.data);
        })
        .catch((err) => {
          alert(err);
        });
    },
    async getAllPosts({ commit }) {
      await axios.get("http://localhost:8022/api/post").then((res) => {
        commit("posts", res.data);
      });
    },
  },
  modules: {
    auth: authStore,
  },
});
