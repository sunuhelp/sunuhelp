import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { QueryClientProvider } from '@tanstack/react-query'
import { queryClient } from './lib/queryClient'
import { useTheme } from './hooks/useTheme'
import HomePage from './pages/HomePage'
import RegisterPage from './pages/RegisterPage'
import { Toaster } from 'sonner'
import './lib/i18n'

function App() {
  useTheme()

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Toaster richColors position="top-center" theme="system" />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/inscription" element={<RegisterPage />} />
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

export default App
