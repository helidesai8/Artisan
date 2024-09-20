import daisyui from 'daisyui';

/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'artisan-white': '#F8FAFC',
        'artisangrey': '#CBD5E1',
        'artisan-blue': '#4F46E5',
        'artisan-dark': '#0F172A'
      },
    },
  },
  plugins: [require("daisyui")],
  daisyui: {
    themes: ["cupcake"]
  },
}
