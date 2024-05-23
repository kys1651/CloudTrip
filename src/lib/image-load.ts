export const imageOrDefault = (src: string | undefined | null) => {
  return src || "http://via.placeholder.com/400x400";
}

const cities: { id: number; name: string; image: string }[] = [
  { id: 1, name: "서울", image: "/cities/seoul.jpg" },
  { id: 2, name: "인천", image: "/cities/incheon.jpg" },
  { id: 3, name: "대전", image: "/cities/daejeon.jpg" },
  { id: 4, name: "대구", image: "/cities/daegu.jpg" },
  { id: 5, name: "광주", image: "/cities/gwangju.jpg" },
  { id: 6, name: "부산", image: "/cities/busan.jpg" },
  { id: 7, name: "울산", image: "/cities/ulsan.jpg" },
  { id: 8, name: "세종특별자치시", image: "/cities/sejong.jpg" },
  { id: 31, name: "경기도", image: "/cities/gyeonggido.jpg" },
  { id: 32, name: "강원도", image: "/cities/gangwondo.jpg" },
  { id: 33, name: "충청북도", image: "/cities/chungcheongbukdo.jpg" },
  { id: 34, name: "충청남도", image: "/cities/chungcheongnamdo.jpg" },
  { id: 35, name: "경상북도", image: "/cities/gyeongsangbukdo.jpg" },
  { id: 36, name: "경상남도", image: "/cities/gyeongsangnamdo.jpg" },
  { id: 37, name: "전라북도", image: "/cities/jeollabukdo.jpg" },
  { id: 38, name: "전라남도", image: "/cities/jeollanamdo.jpg" },
  { id: 39, name: "제주도", image: "/cities/jejuisland.jpg" },
];

export const imageOrCityImage = (src: string | undefined, cityCode: number) => {
  return src || cities.find((city) => city.id === cityCode)!!.image;
}