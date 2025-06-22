-- AI 챗봇 서비스 데이터베이스 초기화 스크립트

-- 데이터베이스 생성 (이미 존재하므로 주석 처리)
-- CREATE DATABASE chatbot_db;

-- 사용자 생성 및 권한 부여 (이미 존재하므로 주석 처리)
-- CREATE USER chatbot_user WITH PASSWORD 'chatbot_password';
-- GRANT ALL PRIVILEGES ON DATABASE chatbot_db TO chatbot_user;

-- 확장 기능 활성화 (UUID 생성용)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 테이블 생성은 JPA가 자동으로 처리하므로 여기서는 기본 설정만 수행

-- 인덱스 생성 (성능 최적화)
-- 이 스크립트는 애플리케이션 시작 후 실행되므로 테이블이 존재할 때만 인덱스 생성
DO $$
BEGIN
    -- users 테이블 인덱스
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'users') THEN
        CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
        CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);
    END IF;
    
    -- threads 테이블 인덱스
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'threads') THEN
        CREATE INDEX IF NOT EXISTS idx_threads_user_id ON threads(user_id);
        CREATE INDEX IF NOT EXISTS idx_threads_created_at ON threads(created_at);
        CREATE INDEX IF NOT EXISTS idx_threads_updated_at ON threads(updated_at);
    END IF;
    
    -- chats 테이블 인덱스
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'chats') THEN
        CREATE INDEX IF NOT EXISTS idx_chats_thread_id ON chats(thread_id);
        CREATE INDEX IF NOT EXISTS idx_chats_created_at ON chats(created_at);
    END IF;
    
    -- feedbacks 테이블 인덱스
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'feedbacks') THEN
        CREATE INDEX IF NOT EXISTS idx_feedbacks_user_id ON feedbacks(user_id);
        CREATE INDEX IF NOT EXISTS idx_feedbacks_chat_id ON feedbacks(chat_id);
        CREATE INDEX IF NOT EXISTS idx_feedbacks_created_at ON feedbacks(created_at);
        CREATE INDEX IF NOT EXISTS idx_feedbacks_is_positive ON feedbacks(is_positive);
        CREATE INDEX IF NOT EXISTS idx_feedbacks_status ON feedbacks(status);
    END IF;
END
$$;
