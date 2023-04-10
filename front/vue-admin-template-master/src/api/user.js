import request from '@/utils/request'

export function login(loginVo) {
  return request({
    url: '/admin/system/index/login',
    method: 'post',
    data: loginVo
  })
}

export function getInfo(token) {
  return request({
    url: '/admin/system/index/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/admin/system/index/logout',
    method: 'post'
  })
}
