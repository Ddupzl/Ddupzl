


CREATE TRIGGER update_competition_status_and_publish_time
    AFTER UPDATE ON db_approvals
    FOR EACH ROW
BEGIN
    IF NEW.result = 1 AND (OLD.result IS NULL OR OLD.result <> 1)
    THEN UPDATE db_competition
    SET
        `status` = 1,
        `publish_time` = NEW.approval_time
    WHERE competition_id = (
        SELECT application.competition_id
        FROM db_applications AS application
        WHERE application.application_id = NEW.approval_id
        LIMIT 1
        );
   END IF;
END



CREATE FUNCTION GetRegistrationCountForCompetition(competition_id_param INT)
    RETURNS INT READS SQL DATA
BEGIN
    DECLARE registration_count INT;
    -- 计算指定竞赛的报名人数
SELECT COUNT(*) INTO registration_count
FROM db_registrations
WHERE competition_id = competition_id_param;

-- 返回报名人数
RETURN registration_count;
END




CREATE PROCEDURE AddNewPost(
    IN p_user_id INT,
    IN p_title VARCHAR(255),
    IN p_content TEXT,
    IN p_post_date DATETIME
)
BEGIN
INSERT INTO db_post (user_id, title, content, post_date)
VALUES (p_user_id, p_title, p_content, p_post_date);
END

