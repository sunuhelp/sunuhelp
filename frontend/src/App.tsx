import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { QueryClientProvider } from '@tanstack/react-query'
import { Toaster } from 'sonner'
import { queryClient } from './lib/queryClient'
import { useTheme } from './hooks/useTheme'
import HomePage from './pages/HomePage'
import RegisterPage from './pages/RegisterPage'
import DashboardPage from './pages/DashboardPage'
import AddBusinessPage from './pages/AddBusinessPage'
import EntityManagePage from './pages/EntityManagePage'
import EntityPhotosPage from './pages/EntityPhotosPage'
import MyBusinessesPage from './pages/MyBusinessesPage'
import { RequireAuth } from './components/RequireAuth'
import './lib/i18n'

function App() {
  useTheme()

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Toaster richColors position="top-center" />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/inscription" element={<RegisterPage />} />
          <Route path="/dashboard" element={<RequireAuth><DashboardPage /></RequireAuth>} />
          <Route path="/mon-etablissement" element={<RequireAuth><AddBusinessPage /></RequireAuth>} />
          <Route path="/mes-etablissements" element={<RequireAuth><MyBusinessesPage /></RequireAuth>} />
          <Route path="/mes-etablissements/:id" element={<RequireAuth><EntityManagePage /></RequireAuth>} />
          <Route path="/mes-etablissements/:id/photos" element={<RequireAuth><EntityPhotosPage /></RequireAuth>} />
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

export default App
