import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { QueryClientProvider } from '@tanstack/react-query'
import { queryClient } from './lib/queryClient'
import { useTheme } from './hooks/useTheme'
import HomePage from './pages/HomePage'
import RegisterPage from './pages/RegisterPage'
import LoginPage from './pages/LoginPage'
import AddBusinessPage from './pages/AddBusinessPage'
import BusinessCreatedPage from './pages/BusinessCreatedPage'
import { RequireAuth } from './components/RequireAuth'
import { Toaster } from 'sonner'
import './lib/i18n'

function App() {
  useTheme()

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Toaster richColors position="top-center" theme="system" />
        <Routes>
          <Route path="/mon-commerce" element={<RequireAuth><AddBusinessPage /></RequireAuth>} />
          <Route path="/commerce-cree" element={<RequireAuth><BusinessCreatedPage /></RequireAuth>} />
          <Route path="/" element={<HomePage />} />
          <Route path="/inscription" element={<RegisterPage />} />
          <Route path="/connexion" element={<LoginPage />} />
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

export default App
