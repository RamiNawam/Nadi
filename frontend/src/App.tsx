import AppRouter from './router';
import { AuthProvider } from './store/authStore';
import './index.css';

function App() {
  return (
    <AuthProvider>
      <AppRouter />
    </AuthProvider>
  );
}

export default App;

